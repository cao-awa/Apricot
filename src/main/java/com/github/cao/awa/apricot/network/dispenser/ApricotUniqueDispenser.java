package com.github.cao.awa.apricot.network.dispenser;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import io.netty.channel.*;
import org.apache.logging.log4j.*;

import java.util.concurrent.*;
import java.util.function.*;

public class ApricotUniqueDispenser {
    private static final Logger LOGGER = LogManager.getLogger("ApricotDispenser");
    private static final Consumer<EchoResultPacket> DO_NO_HANDLE_ECHO = result -> {
    };
    private final ApricotServer server;
    private final ApricotRouter router;
    private RequestHandler handler;
    private long id;
    private long connectTime;
    private PacketJSONBufWriter writer;
    private ChannelHandlerContext context;
    private Channel channel;
    private String disconnectReason = "";

    public ApricotUniqueDispenser(ApricotServer server, ApricotRouter router) {
        this.server = server;
        this.router = router;
    }

    public void channelActive(ChannelHandlerContext context) {
        this.context = context;
        this.channel = context.channel();

        this.writer = new PacketJSONBufWriter(
                this.server,
                this.channel
        );

        this.channel.closeFuture()
                    .addListener(this::onDisconnect);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setConnectTime(long time) {
        this.connectTime = time;
    }

    public ApricotServer getServer() {
        return this.server;
    }

    public ApricotRouter getRouter() {
        return this.router;
    }

    public long getConnectTime() {
        return this.connectTime;
    }

    public RequestHandler getHandler() {
        return this.handler;
    }

    public void setHandler(RequestHandler handler) {
        this.handler = handler;
    }

    public void handle(ReadonlyPacket packet) {
        this.handler.handlePacket(packet);
    }

    public void disconnect(String reason) {
        this.disconnectReason = reason;
        if (this.channel.isOpen()) {
            this.channel.close()
                        .awaitUninterruptibly();
        }
    }

    public void disconnect() {
        disconnect("");
    }

    public void send(WritablePacket packet, Runnable callback) {
        send(packet);
        callback.run();
    }

    public void send(WritablePacket packet) {
        this.server.echo(
                packet,
                DO_NO_HANDLE_ECHO
        );
        packet.writeAndFlush(this.writer);
    }

    public void send(WritablePacket packet, Consumer<EchoResultPacket> echo) {
        this.server.echo(
                packet,
                echo
        );
        packet.writeAndFlush(this.writer);
    }

    public void send(WritablePacket packet, Consumer<EchoResultPacket> echo, Runnable callback) {
        this.server.echo(
                packet,
                echo
        );
        packet.writeAndFlush(this.writer);
        callback.run();
    }

    public void onDisconnect(Future<?> future) {
        LOGGER.info(
                "Proxy '{}' disconnected",
                getId()
        );
        this.router.handleRequest(new JSONObject().fluentPut(
                                              "proxy_id",
                                              getId()
                                      )
                                      .fluentPut(
                                              "connect_time",
                                              getConnectTime()
                                      )
                                      .fluentPut(
                                              "post_type",
                                              "meta_event"
                                      )
                                      .fluentPut(
                                              "meta_event_type",
                                              "lifecycle"
                                      )
                                      .fluentPut(
                                              "sub_type",
                                              "disconnect"
                                      ).fluentPut("disconnect_reason", this.disconnectReason));
    }
}
