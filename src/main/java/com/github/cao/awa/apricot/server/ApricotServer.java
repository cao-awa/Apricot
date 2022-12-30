package com.github.cao.awa.apricot.server;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.config.*;
import com.github.cao.awa.apricot.devlop.clazz.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.cq.factor.*;
import com.github.cao.awa.apricot.message.cq.factor.at.*;
import com.github.cao.awa.apricot.message.cq.factor.image.*;
import com.github.cao.awa.apricot.message.cq.factor.poke.*;
import com.github.cao.awa.apricot.message.cq.factor.replay.*;
import com.github.cao.awa.apricot.network.io.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.factor.invalid.*;
import com.github.cao.awa.apricot.network.packet.factor.message.group.*;
import com.github.cao.awa.apricot.network.packet.factor.message.personal.*;
import com.github.cao.awa.apricot.network.packet.factor.poke.*;
import com.github.cao.awa.apricot.network.packet.factor.response.*;
import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.plugin.accomplish.*;
import com.github.cao.awa.apricot.plugin.firewall.*;
import com.github.cao.awa.apricot.resources.loader.*;
import com.github.cao.awa.apricot.server.service.counter.traffic.*;
import com.github.cao.awa.apricot.server.service.echo.*;
import com.github.cao.awa.apricot.server.service.event.*;
import com.github.cao.awa.apricot.server.service.plugin.*;
import com.github.cao.awa.apricot.utils.io.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class ApricotServer {
    public static final ClazzScanner CLAZZ_SCANNER = new ClazzScanner(Plugin.class);
    private static final Logger LOGGER = LogManager.getLogger("BotServer");
    private final PacketDeserializer packetDeserializers = new PacketDeserializer();
    private final CqDeserializer cqDeserializers = new CqDeserializer();
    private final Configure configs = new Configure(() -> "");
    private final TrafficCounter trafficsCounter = new TrafficCounter("Traffic");
    private final TrafficCounter packetsCounter = new TrafficCounter("Packets");
    private PluginManager plugins;
    private EventManager eventManager;
    private EchoManager echoManager;
    private Executor taskExecutor = Executors.newCachedThreadPool();
    private ApricotServerNetworkIo networkIo;
    private boolean active = true;

    public ApricotServer() {
    }

    public TrafficCounter getTrafficsCounter() {
        return this.trafficsCounter;
    }

    public TrafficCounter getPacketsCounter() {
        return this.packetsCounter;
    }

    public void startup() throws Exception {
        LOGGER.info("Startup apricot bot server");
        setupConfig();
        setupServer();
        setupPlugins();
        setupNetwork();
    }

    public void setupPlugins() {
        this.plugins.loadPlugins();
    }

    public void setupServer() {
        if (this.configs.getBoolean("plugin.threadpool.enable")) {
            this.plugins = new PluginManager(
                    this,
                    Executors.newCachedThreadPool()
            );
        } else {
            this.plugins = new PluginManager(
                    this,
                    Executors.newSingleThreadExecutor()
            );
        }

        if (this.configs.getBoolean("event.threadpool.enable")) {
            this.eventManager = new EventManager(
                    this,
                    Executors.newCachedThreadPool(),
                    this.plugins
            );
        } else {
            this.eventManager = new EventManager(
                    this,
                    Executors.newSingleThreadExecutor(),
                    this.plugins
            );
        }

        if (this.configs.getBoolean("task.threadpool.enable")) {
            this.taskExecutor = Executors.newCachedThreadPool();
        } else {
            this.taskExecutor = Executors.newSingleThreadExecutor();
        }

        if (this.configs.getBoolean("echo.threadpool.enable")) {
            this.echoManager = new EchoManager(Executors.newCachedThreadPool());
        } else {
            this.echoManager = new EchoManager(Executors.newSingleThreadExecutor());
        }
    }

    public void setupConfig() {
        this.configs.init(() -> EntrustEnvironment.receptacle(receptacle -> {
            File config = new File("configs/bot-server.conf");
            if (! config.isFile()) {
                LOGGER.warn("Config not found, generating default config");

                EntrustEnvironment.trys(() -> config.getParentFile()
                                                    .mkdirs());

                receptacle.set(IOUtil.read(ResourcesLoader.getResource("default-config.conf")));

                EntrustEnvironment.operation(
                        new BufferedWriter(new FileWriter(config)),
                        writer -> IOUtil.write(
                                writer,
                                receptacle.get()
                        )
                );
            }

            if (receptacle.get() == null) {
                receptacle.set(IOUtil.read(new BufferedReader(new FileReader(config))));
            }
        }));

        this.configs.setDefault(
                    "superfluous.space.remove",
                    true
            )
                    .setDefault(
                            "event.threadpool.enable",
                            true
                    )
                    .setDefault(
                            "task.threadpool.enable",
                            true
                    )
                    .setDefault(
                            "echo.threadpool.enable",
                            true
                    )
                    .setDefault(
                            "transport.type",
                            "epoll"
                    )
                    .setDefault(
                            "plugin.async.enable",
                            false
                    )
                    .setDefault(
                            "plugin.threadpool.enable",
                            true
                    )
                    .setDefault(
                            "server.port",
                            1145
                    );
    }

    public void setupNetwork() {
        LOGGER.info("Startup apricot bot server network");
        // Setup packet deserializers
        this.packetDeserializers.register(new GroupNormalMessagePacketFactor());
        this.packetDeserializers.register(new GroupAnonymousMessagePacketFactor());
        this.packetDeserializers.register(new PrivateFriendMessagePacketFactor());
        this.packetDeserializers.register(new EchoResultPacketFactor());
        this.packetDeserializers.register(new InvalidDataReceivedPacketFactor());
        this.packetDeserializers.register(new PokeReceivedPacketFactor());

        // Setup CQ deserializers
        this.cqDeserializers.register(new CqImageFactor());
        this.cqDeserializers.register(new CqReplyFactor());
        this.cqDeserializers.register(new CqAtFactor());
        this.cqDeserializers.register(new CqPokeFactor());

        // Setup network io
        this.networkIo = new ApricotServerNetworkIo(this);

        submitTask(() -> EntrustEnvironment.trys(() -> this.networkIo.start(configs.getInteger("server.port"))));
    }

    public void submitTask(Runnable runnable) {
        this.taskExecutor.execute(runnable);
    }

    @NotNull
    public ReadonlyPacket createPacket(JSONObject json) {
        return this.packetDeserializers.deserializer(
                this,
                json
        );
    }

    public MessageElement createCq(List<String> elements) {
        return this.cqDeserializers.deserializer(
                this,
                elements
        );
    }

    public void registerPlugin(AccomplishPlugin plugin) {
        this.plugins.registerAccomplish(plugin);
    }

    public void registerFirewall(FirewallPlugin plugin) {
        this.plugins.registerFirewall(plugin);
    }

    /**
     * Let an event be fired.
     *
     * @param event
     *         event
     * @author 草二号机
     * @since 1.0.0
     */
    public void fireEvent(Event<?> event) {
        this.eventManager.fireEvent(event);
    }

    public void echo(Packet packet, Consumer<EchoResultPacket> action) {
        echo(
                packet.getIdentifier(),
                action
        );
    }

    public void echo(String identifier, Consumer<EchoResultPacket> action) {
        this.echoManager.echo(
                identifier,
                action
        );
    }

    public void echo(String identifier, EchoResultPacket packet) {
        this.echoManager.echo(
                identifier,
                packet
        );
    }

    public boolean useEpoll() {
        return this.configs.get("transport.type")
                           .equals("epoll");
    }

    public boolean shouldCaverMessage() {
        return this.configs.getBoolean("superfluous.space.remove");
    }

    public boolean shouldAsyncLoadPlugins() {
        return this.configs.getBoolean("plugin.async.enable");
    }

    public synchronized void shutdown() {
        if (this.active) {
            LOGGER.info("Apricot bot server shutting down");
            this.networkIo.shutdown();
            this.eventManager.shutdown();
            this.echoManager.shutdown();
            this.plugins.shutdown();
            LOGGER.info("Apricot bot server is shutdown");
            System.exit(0);
        }
    }
}
