package com.github.cao.awa.apricot.server;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.config.*;
import com.github.cao.awa.apricot.event.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.cq.factor.*;
import com.github.cao.awa.apricot.message.cq.factor.image.*;
import com.github.cao.awa.apricot.network.io.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.factor.message.*;
import com.github.cao.awa.apricot.network.packet.factor.response.*;
import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.resources.loader.*;
import com.github.cao.awa.apricot.server.service.counter.traffic.*;
import com.github.cao.awa.apricot.server.service.echo.*;
import com.github.cao.awa.apricot.server.service.event.*;
import com.github.cao.awa.apricot.utils.io.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import it.unimi.dsi.fastutil.objects.*;
import org.apache.logging.log4j.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class ApricotServer {
    private static final Logger LOGGER = LogManager.getLogger("BotServer");
    private final Map<UUID, Plugin> plugins = new Object2ObjectOpenHashMap<>();
    private final PacketDeserializer packetDeserializers = new PacketDeserializer();
    private final CqDeserializer cqDeserializers = new CqDeserializer();
    private final Configure configs = new Configure(() -> "");
    private EventManager eventManager;
    private EchoManager echoManager;
    private final TrafficCounter trafficsCounter = new TrafficCounter("Traffic");
    private final TrafficCounter packetsCounter = new TrafficCounter("Packets");
    private Executor taskExecutor = Executors.newCachedThreadPool();
    private ApricotServerNetworkIo networkIo;
    public ApricotServer() {
    }

    public TrafficCounter getTrafficsCounter() {
        return this.trafficsCounter;
    }

    public TrafficCounter getPacketsCounter() {
        return this.packetsCounter;
    }

    public void startup() throws Exception {
        setupServer();
        setupNetwork();
    }

    public void setupServer() {
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

        if (this.configs.get("event.threadpool.enable")
                        .equals("true")) {
            this.eventManager = new EventManager(
                    Executors.newCachedThreadPool(),
                    this.plugins
            );
        } else {
            this.eventManager = new EventManager(
                    Executors.newSingleThreadExecutor(),
                    this.plugins
            );
        }

        if (this.configs.get("task.threadpool.enable")
                        .equals("true")) {
            this.taskExecutor = Executors.newCachedThreadPool();
        } else {
            this.taskExecutor = Executors.newSingleThreadExecutor();
        }

        if (this.configs.get("echo.threadpool.enable")
                        .equals("true")) {
            this.echoManager = new EchoManager(Executors.newCachedThreadPool());
        } else {
            this.echoManager = new EchoManager(Executors.newSingleThreadExecutor());
        }
    }

    public void setupNetwork() throws Exception {
        // Setup packet deserializers
        this.packetDeserializers.register(new MessageReceivedPacketFactor());
        this.packetDeserializers.register(new EchoResultPacketFactor());

        // Setup CQ deserializers
        this.cqDeserializers.register(new CqImageFactor());

        // Setup network io
        this.networkIo = new ApricotServerNetworkIo(this);

        this.networkIo.start(Integer.parseInt(configs.get("server.port")));
    }

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

    public void registerPlugin(Plugin plugin) {
        this.plugins.put(
                plugin.getUuid(),
                plugin
        );
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

    public void submitTask(Runnable runnable) {
        this.taskExecutor.execute(runnable);
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
}
