package com.github.cao.awa.apricot.server;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.config.*;
import com.github.cao.awa.apricot.event.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.cq.factor.*;
import com.github.cao.awa.apricot.message.cq.factor.image.*;
import com.github.cao.awa.apricot.network.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.factor.message.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.resources.loader.*;
import com.github.cao.awa.apricot.utils.io.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import it.unimi.dsi.fastutil.objects.*;
import org.apache.logging.log4j.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ApricotServer {
    private static final Logger LOGGER = LogManager.getLogger("BotServer");
    private final Map<UUID, Plugin> plugins = new Object2ObjectOpenHashMap<>();
    private final PacketDeserializer packetDeserializers = new PacketDeserializer();
    private final CqDeserializer cqDeserializers = new CqDeserializer();
    private final Configure configs = new Configure(() -> "");
    private Executor eventExecutor = Executors.newCachedThreadPool();
    private Executor taskExecutor = Executors.newCachedThreadPool();
    private ApricotServerNetworkIo networkIo;

    public ApricotServer() {
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
            this.eventExecutor = Executors.newCachedThreadPool();
        } else {
            this.eventExecutor = Executors.newSingleThreadExecutor();
        }
    }

    public void setupNetwork() throws Exception {
        // Setup packet deserializers
        this.packetDeserializers.register(new MessageReceivedPacketFactor());

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
    public void fireEvent(Event event) {
        this.plugins.values()
                    .forEach(plugin -> this.eventExecutor.execute(() -> plugin.fireEvent(event)));
    }

    public void submitTask(Runnable runnable) {
        this.taskExecutor.execute(runnable);
    }
}
