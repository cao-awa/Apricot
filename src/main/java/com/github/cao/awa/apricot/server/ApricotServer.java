package com.github.cao.awa.apricot.server;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.config.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.factor.*;
import com.github.cao.awa.apricot.message.element.cq.factor.at.*;
import com.github.cao.awa.apricot.message.element.cq.factor.image.*;
import com.github.cao.awa.apricot.message.element.cq.factor.poke.*;
import com.github.cao.awa.apricot.message.element.cq.factor.replay.*;
import com.github.cao.awa.apricot.network.io.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.factor.invalid.*;
import com.github.cao.awa.apricot.network.packet.factor.member.change.decrease.bot.*;
import com.github.cao.awa.apricot.network.packet.factor.member.change.decrease.kick.*;
import com.github.cao.awa.apricot.network.packet.factor.member.change.decrease.leave.*;
import com.github.cao.awa.apricot.network.packet.factor.member.change.increase.approve.*;
import com.github.cao.awa.apricot.network.packet.factor.member.change.increase.invite.*;
import com.github.cao.awa.apricot.network.packet.factor.message.group.*;
import com.github.cao.awa.apricot.network.packet.factor.message.personal.*;
import com.github.cao.awa.apricot.network.packet.factor.message.recall.gruop.*;
import com.github.cao.awa.apricot.network.packet.factor.message.recall.personal.*;
import com.github.cao.awa.apricot.network.packet.factor.meta.lifecycle.*;
import com.github.cao.awa.apricot.network.packet.factor.name.card.*;
import com.github.cao.awa.apricot.network.packet.factor.name.title.*;
import com.github.cao.awa.apricot.network.packet.factor.poke.*;
import com.github.cao.awa.apricot.network.packet.factor.response.*;
import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import com.github.cao.awa.apricot.plugin.accomplish.*;
import com.github.cao.awa.apricot.plugin.firewall.*;
import com.github.cao.awa.apricot.resources.loader.*;
import com.github.cao.awa.apricot.server.service.counter.traffic.*;
import com.github.cao.awa.apricot.server.service.echo.*;
import com.github.cao.awa.apricot.server.service.event.*;
import com.github.cao.awa.apricot.server.service.plugin.*;
import com.github.cao.awa.apricot.thread.pool.*;
import com.github.cao.awa.apricot.utils.io.*;
import com.github.cao.awa.apricot.utils.times.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public class ApricotServer {
    public static final AtomicLong performance = new AtomicLong();
    private static final Logger LOGGER = LogManager.getLogger("BotServer");
    private final PacketDeserializer packetDeserializers = new PacketDeserializer();
    private final CqDeserializer cqDeserializers = new CqDeserializer();
    private final Configure configs = new Configure(() -> "");
    private final TrafficCounter trafficsCounter = new TrafficCounter("Traffic");
    private final TrafficCounter packetsCounter = new TrafficCounter("Packets");
    private final boolean active = true;
    private PluginManager plugins;
    private EventManager eventManager;
    private EchoManager echoManager;
    private ExecutorEntrust taskExecutor = new ExecutorEntrust(Executors.newCachedThreadPool());
    private ApricotServerNetworkIo networkIo;

    public ApricotServer() {
    }

    public ApricotServerNetworkIo getNetworkIo() {
        return networkIo;
    }

    public TrafficCounter getTrafficsCounter() {
        return this.trafficsCounter;
    }

    public TrafficCounter getPacketsCounter() {
        return this.packetsCounter;
    }

    public void startup() {
        performance.set(TimeUtil.millions());
        LOGGER.info("Startup apricot bot server");
        setupDirectories();
        setupConfig();
        setupServer();
        setupPlugins();
        setupNetwork();
    }

    public void setupDirectories() {
        boolean config = new File("configs").mkdirs();
        boolean plugins = new File("plugins").mkdirs();
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
            this.taskExecutor = new ExecutorEntrust(Executors.newCachedThreadPool());
        } else {
            this.taskExecutor = new ExecutorEntrust(Executors.newSingleThreadExecutor());
        }

        if (this.configs.getBoolean("echo.threadpool.enable")) {
            this.echoManager = new EchoManager(Executors.newCachedThreadPool());
        } else {
            this.echoManager = new EchoManager(Executors.newSingleThreadExecutor());
        }
    }

    public void setupConfig() {
        this.configs.init(() -> EntrustEnvironment.receptacle(receptacle -> {
            try {
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
            } catch (Exception e) {
                e.printStackTrace();
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
        this.packetDeserializers.register(new ProxyConnectPacketFactor());
        this.packetDeserializers.register(new ProxyDisconnectPacketFactor());
        this.packetDeserializers.register(new GroupNormalMessagePacketFactor());
        this.packetDeserializers.register(new GroupAnonymousMessagePacketFactor());
        this.packetDeserializers.register(new GroupMessageRecallPacketFactor());
        this.packetDeserializers.register(new GroupNameChangedReceivedPacketFactor());
        this.packetDeserializers.register(new GroupMemberApprovedPacketFactor());
        this.packetDeserializers.register(new GroupMemberInvitedPacketFactor());
        this.packetDeserializers.register(new GroupMemberLeavedPacketFactor());
        this.packetDeserializers.register(new GroupMemberKickedPacketFactor());
        this.packetDeserializers.register(new BotDiedFromGroupPacketFactor());
        this.packetDeserializers.register(new GroupTitleChangedReceivedPacketFactor());
        this.packetDeserializers.register(new PrivateFriendMessagePacketFactor());
        this.packetDeserializers.register(new PrivateMessageRecallPacketFactor());
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

        submitTask(
                "ApricotNetwork",
                () -> EntrustEnvironment.trys(
                        () -> this.networkIo.start(configs.getInteger("server.port")),
                        ex -> {
                            LOGGER.error(
                                    "Apricot network failed to startup",
                                    ex
                            );
                            shutdown();
                        }
                )
        );
    }

    public void submitTask(String entrust, Runnable runnable) {
        this.taskExecutor.execute(
                entrust,
                runnable
        );
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

    public void echo(WritablePacket packet, Consumer<EchoResultPacket> action) {
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
}
