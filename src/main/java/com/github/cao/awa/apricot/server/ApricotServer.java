package com.github.cao.awa.apricot.server;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.banner.*;
import com.github.cao.awa.apricot.config.*;
import com.github.cao.awa.apricot.config.plugin.handler.*;
import com.github.cao.awa.apricot.database.message.store.*;
import com.github.cao.awa.apricot.database.simple.serial.*;
import com.github.cao.awa.apricot.event.receive.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.factor.*;
import com.github.cao.awa.apricot.message.element.cq.factor.at.*;
import com.github.cao.awa.apricot.message.element.cq.factor.face.*;
import com.github.cao.awa.apricot.message.element.cq.factor.forward.*;
import com.github.cao.awa.apricot.message.element.cq.factor.image.*;
import com.github.cao.awa.apricot.message.element.cq.factor.json.*;
import com.github.cao.awa.apricot.message.element.cq.factor.poke.*;
import com.github.cao.awa.apricot.message.element.cq.factor.record.*;
import com.github.cao.awa.apricot.message.element.cq.factor.replay.*;
import com.github.cao.awa.apricot.message.element.cq.factor.video.*;
import com.github.cao.awa.apricot.message.element.cq.factor.xml.*;
import com.github.cao.awa.apricot.message.store.*;
import com.github.cao.awa.apricot.network.io.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.factor.add.friend.*;
import com.github.cao.awa.apricot.network.packet.factor.add.group.*;
import com.github.cao.awa.apricot.network.packet.factor.invalid.*;
import com.github.cao.awa.apricot.network.packet.factor.member.change.decrease.bot.*;
import com.github.cao.awa.apricot.network.packet.factor.member.change.decrease.kick.*;
import com.github.cao.awa.apricot.network.packet.factor.member.change.decrease.leave.*;
import com.github.cao.awa.apricot.network.packet.factor.member.change.increase.approve.*;
import com.github.cao.awa.apricot.network.packet.factor.member.change.increase.invite.*;
import com.github.cao.awa.apricot.network.packet.factor.message.group.received.*;
import com.github.cao.awa.apricot.network.packet.factor.message.group.sent.*;
import com.github.cao.awa.apricot.network.packet.factor.message.personal.received.*;
import com.github.cao.awa.apricot.network.packet.factor.message.personal.sent.*;
import com.github.cao.awa.apricot.network.packet.factor.message.recall.gruop.*;
import com.github.cao.awa.apricot.network.packet.factor.message.recall.personal.*;
import com.github.cao.awa.apricot.network.packet.factor.meta.lifecycle.*;
import com.github.cao.awa.apricot.network.packet.factor.mute.issue.*;
import com.github.cao.awa.apricot.network.packet.factor.mute.issue.all.*;
import com.github.cao.awa.apricot.network.packet.factor.mute.issue.personal.*;
import com.github.cao.awa.apricot.network.packet.factor.mute.lift.*;
import com.github.cao.awa.apricot.network.packet.factor.mute.lift.all.*;
import com.github.cao.awa.apricot.network.packet.factor.mute.lift.personal.*;
import com.github.cao.awa.apricot.network.packet.factor.name.card.*;
import com.github.cao.awa.apricot.network.packet.factor.name.title.*;
import com.github.cao.awa.apricot.network.packet.factor.poke.*;
import com.github.cao.awa.apricot.network.packet.factor.response.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.network.packet.receive.response.message.get.*;
import com.github.cao.awa.apricot.network.packet.send.message.get.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.plugin.internal.bus.*;
import com.github.cao.awa.apricot.plugin.internal.core.lawn.*;
import com.github.cao.awa.apricot.resource.dispenser.*;
import com.github.cao.awa.apricot.resource.loader.*;
import com.github.cao.awa.apricot.server.service.counter.traffic.*;
import com.github.cao.awa.apricot.server.service.echo.*;
import com.github.cao.awa.apricot.server.service.event.*;
import com.github.cao.awa.apricot.server.service.plugin.*;
import com.github.cao.awa.apricot.server.service.task.*;
import com.github.cao.awa.apricot.task.intensive.*;
import com.github.cao.awa.apricot.thread.pool.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.cao.awa.apricot.util.io.*;
import com.github.cao.awa.apricot.util.time.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.*;
import org.iq80.leveldb.*;
import org.iq80.leveldb.impl.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public class ApricotServer {
    public static final String DATABASE_PATH = "databases/";
    public static final String MESSAGE_DATABASE_PATH = DATABASE_PATH + "messages/head_office/";
    public static final String RESOURCE_DATABASE_PATH = DATABASE_PATH + "resources/";
    public static final String VERSION_SUFFIX = "-preview";
    public static final String VERSION = "1.0.0" + VERSION_SUFFIX;
    private static final Logger LOGGER = LogManager.getLogger("BotServer");
    private final AtomicLong startupPerformance = new AtomicLong();
    private final PacketDeserializer packetDeserializers = new PacketDeserializer();
    private final CqDeserializer cqDeserializers = new CqDeserializer();
    private final Configure configs = new Configure(() -> "");
    private final TrafficCounter trafficsCounter = new TrafficCounter("Traffic");
    private final TrafficCounter packetsCounter = new TrafficCounter("Packets");
    private final Map<String, SerialLongKvDatabase> relationalDatabases = ApricotCollectionFactor.newHashMap();
    private final ResourcesDispenser resourcesDispenser = new ResourcesDispenser(
            RESOURCE_DATABASE_PATH,
            this
    );
    private final ApsConfig apsConfig = new ApsConfig(
            "configs/plugins/core/Apricot/Apricot.json",
            "plugins/Apricot/Apricot.json"
    );
    public LawnBus eventBus;
    private boolean active = false;
    private PluginManager plugins;
    private EventManager eventManager;
    private EchoManager echoManager;
    private TaskManager cpuTaskManager;
    private TaskManager ioTaskManager;
    private ApricotServerNetworkIo networkIo;
    private MessageDatabase messagesHeadOffice;

    public ApricotServer() {
    }

    public ApsConfig apsConfig() {
        return this.apsConfig;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }

    public ResourcesDispenser getResourcesDispenser() {
        return this.resourcesDispenser;
    }

    public void download(String name, String url, Consumer<Boolean> callback) {
        this.resourcesDispenser.download(
                name,
                url,
                callback
        );
    }

    public void download(String name, String url) {
        this.resourcesDispenser.download(
                name,
                url
        );
    }

    @NotNull
    public GetMessageResponsePacket getMessage(ApricotProxy proxy, long messageId) {
        long ownId = this.messagesHeadOffice.getConvert(messageId);
        MessageStore store = this.messagesHeadOffice.get(ownId);
        if (store == null) {
            Receptacle<GetMessageResponsePacket> response = Receptacle.of();

            proxy.send(
                    new GetMessagePacket(messageId),
                    response::set
            );

            long timeout = 2000;

            long await = TimeUtil.millions();

            while (response.get() == null && TimeUtil.processMillion(await) < timeout) {
                TimeUtil.coma(15);
            }

            if (response.get() == null) {
                return new GetMessageResponsePacket(
                        new BasicMessageSender(
                                - 1,
                                ""
                        ),
                        new AssembledMessage(),
                        MessageType.GROUP,
                        - 1,
                        - 1,
                        - 1L,
                        - 1
                );
            }
            GetMessageResponsePacket packet = response.get();
            this.messagesHeadOffice.set(
                    packet.getOwnId(),
                    new MessageStore(
                            packet.getType(),
                            packet.getMessage(),
                            packet.getSender()
                                  .getSenderId(),
                            packet.getTargetId(),
                            packet.getMessageId(),
                            packet.getTimestamp(),
                            false
                    )
            );
            return packet;
        }
        return new GetMessageResponsePacket(
                new BasicMessageSender(
                        store.getSenderId(),
                        ""
                ),
                store.getMessage(),
                store.getType(),
                store.getTargetId(),
                store.getMessageId(),
                ownId,
                store.getTimestamp()
        );
    }

    public File getResource(String name) {
        return this.resourcesDispenser.get(name);
    }

    public ApricotServerNetworkIo getNetworkIo() {
        return this.networkIo;
    }

    public TrafficCounter getTrafficsCounter() {
        return this.trafficsCounter;
    }

    public TrafficCounter getPacketsCounter() {
        return this.packetsCounter;
    }

    public void startup() throws IOException {
        startupPerformance.set(TimeUtil.millions());
        LOGGER.info(
                "Startup apricot {}",
                VERSION
        );
        setupDirectories();
        printBanner();
        if (VERSION.endsWith("-preview")) {
            LOGGER.warn(
                    "The apricot in {} is a preview version, not stabled, please do careful to bugs",
                    VERSION
            );
        }
        setupConfig();
        setupServer();
        setupPlugins();
        setupDatabase();
        setupNetwork();
        this.active = true;
    }

    private void printBanner() throws IOException {
        Banner banner = new Banner(IOUtil.read(ResourcesLoader.get("apricot.banner")));
        banner.forEach(line -> {
            LOGGER.info(line);
        });
    }

    public void setupDatabase() throws IOException {
        if (this.active) {
            LOGGER.warn("Database already setup, do not setup again");
            return;
        }
        try {
            this.messagesHeadOffice = new MessageDatabase(
                    this,
                    new Iq80DBFactory().open(
                            new File(MESSAGE_DATABASE_PATH + "/head"),
                            new Options().createIfMissing(true)
                                         .writeBufferSize(1048560)
                                         .compressionType(CompressionType.SNAPPY)
                    ),
                    new Iq80DBFactory().open(
                            new File(MESSAGE_DATABASE_PATH + "/convert"),
                            new Options().createIfMissing(true)
                                         .writeBufferSize(1048560)
                                         .compressionType(CompressionType.SNAPPY)
                    )
            );
        } catch (Exception e) {
            LOGGER.warn("Failed setup databases");
            throw e;
        }
    }

    public void setupDirectories() {
        boolean config = new File("configs").mkdirs();
        boolean plugins = new File("plugins").mkdirs();
    }

    public void setupPlugins() {
        if (this.active) {
            LOGGER.warn("Plugins already setup, do not setup again");
            return;
        }
        LOGGER.info("Loading internal plugins");
        this.eventBus = new LawnBus();
        this.plugins.register(new LawnCore());
        this.plugins.register(this.eventBus);
        this.plugins.loadPlugins();
    }

    public void setupServer() {
        if (this.active) {
            LOGGER.warn("Server already setup, do not setup again");
            return;
        }
        this.plugins = new PluginManager(
                this,
                ExecutorFactor.intensiveIo()
        );

        this.eventManager = new EventManager(
                this,
                ExecutorFactor.intensiveIo(),
                this.plugins
        );

        this.echoManager = new EchoManager(ExecutorFactor.intensiveIo());

        this.cpuTaskManager = new TaskManager(
                new ExecutorEntrust(ExecutorFactor.intensiveCpu()),
                new ExecutorEntrust(ExecutorFactor.scheduled(4))
        );

        this.ioTaskManager = new TaskManager(
                new ExecutorEntrust(ExecutorFactor.intensiveIo()),
                new ExecutorEntrust(ExecutorFactor.scheduled(4))
        );

    }

    public void setupConfig() {
        if (this.active) {
            LOGGER.warn("Config already setup, do not setup again, call 'reloadConfig' to reload");
            return;
        }
        this.configs.setDefault(
                    "SO_NO_SUPERFLUOUS",
                    true
            )
                    .setDefault(
                            "TRANSPORT_TYPE",
                            "epoll"
                    )
                    .setDefault(
                            "SO_ASYNC_PLUGIN",
                            false
                    )
                    .setDefault(
                            "SERVER_PORT",
                            1145
                    )
                    .setDefault(
                            "SO_BINARY_MESSAGE",
                            true
                    );
        reloadConfig();
    }

    public void reloadConfig() {
        this.configs.init(() -> EntrustEnvironment.receptacle(receptacle -> {
            try {
                File config = new File("configs/bot-server.conf");
                if (! config.isFile()) {
                    LOGGER.warn("Config not found, generating default config");

                    EntrustEnvironment.trys(() -> config.getParentFile()
                                                        .mkdirs());

                    receptacle.set(IOUtil.read(ResourcesLoader.get("default-config.conf")));

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
    }

    public void setupNetwork() {
        if (this.active) {
            LOGGER.warn("Network already setup, do not setup again");
            return;
        }
        LOGGER.info("Startup apricot network");
        // Setup packet deserializers
        EntrustEnvironment.operation(
                this.packetDeserializers,
                deserializer -> {
                    deserializer.register(new ProxyConnectPacketFactor());
                    deserializer.register(new ProxyDisconnectPacketFactor());
                    deserializer.register(new GroupNormalMessageSentPacketFactor());
                    deserializer.register(new GroupAnonymousSentMessagePacketFactor());
                    deserializer.register(new GroupNormalMessagePacketFactor());
                    deserializer.register(new GroupAnonymousMessagePacketFactor());
                    deserializer.register(new GroupMessageRecallPacketFactor());
                    deserializer.register(new GroupNameChangedReceivedPacketFactor());
                    deserializer.register(new GroupMemberApprovedPacketFactor());
                    deserializer.register(new GroupMemberInvitedPacketFactor());
                    deserializer.register(new GroupMemberLeavedPacketFactor());
                    deserializer.register(new GroupMemberKickedPacketFactor());
                    deserializer.register(new AddGroupReceivedPacketFactor());
                    deserializer.register(new InviteGroupReceivedPacketFactor());
                    deserializer.register(new AddFriendReceivedPacketFactor());
                    deserializer.register(new IssueGroupMuteReceivedPacketFactor());
                    deserializer.register(new IssueGroupAllMuteReceivedPacketFactor());
                    deserializer.register(new IssueGroupPersonalMuteReceivedPacketFactor());
                    deserializer.register(new LiftGroupMuteReceivedPacketFactor());
                    deserializer.register(new LiftGroupAllMuteReceivedPacketFactor());
                    deserializer.register(new LiftGroupPersonalMuteReceivedPacketFactor());
                    deserializer.register(new BotDiedFromGroupPacketFactor());
                    deserializer.register(new GroupTitleChangedReceivedPacketFactor());
                    deserializer.register(new PrivateFriendMessageReceivedPacketFactor());
                    deserializer.register(new PrivateTemporaryMessageReceivedPacketFactor());
                    deserializer.register(new PrivateFriendMessageSentPacketFactor());
                    deserializer.register(new PrivateTemporaryMessageSentPacketFactor());
                    deserializer.register(new PrivateMessageRecallPacketFactor());
                    deserializer.register(new EchoResultPacketFactor());
                    deserializer.register(new InvalidDataReceivedPacketFactor());
                    deserializer.register(new PokeReceivedPacketFactor());
                }
        );

        // Setup CQ deserializers
        EntrustEnvironment.operation(
                this.cqDeserializers,
                deserializer -> {
                    deserializer.register(new CqReplyFactor());
                    deserializer.register(new CqAtFactor());
                    deserializer.register(new CqPokeFactor());
                    deserializer.register(new CqFaceFactor());
                    deserializer.register(new CqImageFactor());
                    deserializer.register(new CqForwardFactor());
                    deserializer.register(new CqXmlFactor());
                    deserializer.register(new CqJsonFactor());
                    deserializer.register(new CqRecordFactor());
                    deserializer.register(new CqVideoFactor());
                }
        );

        // Setup network io
        this.networkIo = new ApricotServerNetworkIo(this);

        intensiveIo().execute(() -> EntrustEnvironment.trys(
                () -> this.networkIo.start(configs.getInteger("SERVER_PORT")),
                ex -> {
                    LOGGER.error(
                            "Apricot network failed to startup",
                            ex
                    );
                    shutdown();
                }
        ));
    }

    public TaskManager intensiveIo() {
        return this.ioTaskManager;
    }

    public synchronized void shutdown() {
        if (this.active) {
            this.active = false;
            LOGGER.info("Apricot shutting down");
            this.networkIo.shutdown();
            this.eventManager.shutdown();
            this.echoManager.shutdown();
            this.plugins.shutdown();
            LOGGER.info("Apricot has been shutdown");
            System.exit(0);
        }
    }

    public TaskManager intensiveCpu() {
        return this.cpuTaskManager;
    }

    public void execute(Runnable runnable) {
        this.cpuTaskManager.execute(runnable);
    }

    public void execute(IntensiveType type, Runnable runnable) {
        if (type == IntensiveType.CPU) {
            this.cpuTaskManager.execute(runnable);
        } else {
            this.ioTaskManager.execute(runnable);
        }
    }

    public <T> CompletableFuture<T> future(Supplier<T> runnable) {
        return this.cpuTaskManager.future(runnable);
    }

    public MessageDatabase getMessagesHeadOffice() {
        return this.messagesHeadOffice;
    }

    public synchronized SerialLongKvDatabase getRelationalDatabase(long botId, long targetId) {
        String path = DATABASE_PATH + "messages/relational/" + botId + "/" + targetId + ".db";
        SerialLongKvDatabase rel = this.relationalDatabases.get(path);
        if (rel == null) {
            rel = new SerialLongKvDatabase(path);
            this.relationalDatabases.put(
                    path,
                    rel
            );
        }
        return rel;
    }

    public long getStartupTime() {
        return startupPerformance.get();
    }

    public void schedule(long delay, TimeUnit unit, Runnable runnable) {
        this.cpuTaskManager.schedule(
                delay,
                unit,
                runnable
        );
    }

    public void schedule(long delay, long interval, TimeUnit unit, Runnable runnable) {
        this.cpuTaskManager.schedule(
                delay,
                interval,
                unit,
                runnable
        );
    }

    @NotNull
    public ReadonlyPacket createPacket(JSONObject json) {
        return this.packetDeserializers.deserializerPacket(
                this,
                json
        );
    }

    @Nullable
    @Unsupported
    public ReadonlyPacket createResponse(String type, JSONObject json) {
        return this.packetDeserializers.deserializerResponse(
                this,
                type,
                json
        );
    }

    @NotNull
    public ReadonlyPacket createPacket(String name, JSONObject json) {
        return this.packetDeserializers.deserializer(
                name,
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
        this.plugins.register(plugin);
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

    public void echo(WritablePacket<? extends ResponsePacket> packet, Consumer<EchoResultPacket> action) {
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
        return this.configs.get("TRANSPORT_TYPE")
                           .equals("epoll");
    }

    public boolean shouldCaverMessage() {
        return this.configs.getBoolean("SO_NO_SUPERFLUOUS");
    }

    public boolean shouldAsyncLoadPlugins() {
        return this.configs.getBoolean("SO_ASYNC_PLUGIN");
    }

    public Collection<Plugin> getPlugins() {
        return this.plugins.getPlugins();
    }

    public boolean useBinaryMessage() {
        return this.configs.getBoolean("SO_BINARY_MESSAGE");
    }
}
