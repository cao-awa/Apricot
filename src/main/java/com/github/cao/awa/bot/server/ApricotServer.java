package com.github.cao.awa.bot.server;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.bot.config.*;
import com.github.cao.awa.bot.event.*;
import com.github.cao.awa.bot.event.pipeline.*;
import com.github.cao.awa.bot.network.*;
import com.github.cao.awa.bot.network.packet.*;
import com.github.cao.awa.bot.network.packet.factor.*;
import com.github.cao.awa.bot.network.packet.factor.message.*;
import com.github.cao.awa.bot.plugin.*;
import com.github.cao.awa.bot.resources.loader.*;
import com.github.cao.awa.bot.utils.io.*;
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
    private Executor executor = Executors.newCachedThreadPool();
    private ApricotServerNetworkIo networkIo;
    private final Configure configs = new Configure(() -> "");

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

        if (this.configs.get("threadpool.enable").equals("true")) {
            this.executor = Executors.newCachedThreadPool();
        } else {
            this.executor = Executors.newSingleThreadExecutor();
        }
    }

    public void setupNetwork() throws Exception {
        // Setup packet deserializers
        this.packetDeserializers.register(new MessageReceivedPacketFactor());

        // Setup network io
        this.networkIo = new ApricotServerNetworkIo(this);

        this.networkIo.start(Integer.parseInt(configs.get("server.port")));
    }

    public ReadonlyPacket createPacket(JSONObject json) {
        return this.packetDeserializers.deserializer(json);
    }

    public void registerPlugin(Plugin plugin) {
        this.plugins.put(
                plugin.getUuid(),
                plugin
        );
    }

    public void fireEvent(Event event) {
        this.executor.execute(() -> {
            PipelineController controller = new PipelineController();
            this.plugins.values()
                        .forEach(plugin -> {
                            plugin.fireEvent(event);
                        });
        });
    }
}
