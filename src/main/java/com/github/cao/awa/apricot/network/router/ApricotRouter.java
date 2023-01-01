package com.github.cao.awa.apricot.network.router;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.connection.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.thread.*;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

/**
 * Request handler of apricot bot server.
 *
 * @author 草二号机
 * @since 1.0.0
 */
public class ApricotRouter extends NetworkRouter {
    private static final Logger LOGGER = LogManager.getLogger("ApricotRouter");
    private final @NotNull ApricotServer server;
    private final @NotNull ApricotProxy proxy;
    private final @NotNull StringBuilder fragment = new StringBuilder();
    private final ApricotUniqueDispenser dispenser;
    private ChannelHandlerContext context;
    private Channel channel;

    public ApricotRouter(@NotNull ApricotServer server) {
        this.server = server;
        this.proxy = new ApricotProxy(
                this,
                server
        );
        this.dispenser = new ApricotUniqueDispenser(
                server,
                this
        );
        this.dispenser.setHandler(new ApricotHandshakeHandler(this.dispenser));
    }

    public ApricotUniqueDispenser getDispenser() {
        return this.dispenser;
    }

    @NotNull
    public ApricotProxy getProxy() {
        return this.proxy;
    }

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        super.channelActive(context);
        this.context = context;
        this.channel = context.channel();
        this.dispenser.channelActive(context);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, WebSocketFrame frame) {
        this.server.getTrafficsCounter()
                   .in(frame.content()
                            .writerIndex());
        this.server.getPacketsCounter()
                   .in(1);
        handleFragment(frame);
    }

    private void handleFragment(WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame textFrame) {
            if (frame.isFinalFragment()) {
                handleFrame(textFrame);
                LOGGER.debug("Handled single fragment packet");
            } else {
                if (this.fragment.length() == 0) {
                    this.fragment.append(textFrame.text());
                } else {
                    LOGGER.warn("Occurs unexpected fragment appended");
                }
            }
        } else if (frame instanceof ContinuationWebSocketFrame continuationFrame) {
            this.fragment.append(continuationFrame.text());
            LOGGER.debug("Fragment appended");

            if (continuationFrame.isFinalFragment()) {
                handleFrame(new TextWebSocketFrame(this.fragment.toString()));
                this.fragment.setLength(0);
            }
        } else {
            LOGGER.warn("Occurs unexpected fragment appender received");
        }
    }

    public void handleFrame(TextWebSocketFrame frame) {
        handleRequest(JSONObject.parseObject(frame.text()));
    }

    public void handleRequest(JSONObject request) {
        this.server.submitTask(
                "ApricotRouter",
                () -> this.dispenser.handle(this.server.createPacket(request))
        );
    }

    public void send(WritablePacket packet, Runnable callback) {
        this.dispenser.send(
                packet,
                callback
        );
    }

    public void send(WritablePacket packet) {
        this.dispenser.send(packet);
    }

    public void send(WritablePacket packet, Consumer<EchoResultPacket> echo) {
        this.dispenser.send(
                packet,
                echo
        );
    }

    public void send(WritablePacket packet, Consumer<EchoResultPacket> echo, Runnable callback) {
        this.dispenser.send(
                packet,
                echo,
                callback
        );
    }

    public void disconnect() {
        this.dispenser.disconnect();
    }

    public void disconnect(String reason) {
        this.dispenser.disconnect(reason);
    }

    public Channel getChannel() {
        return this.channel;
    }
}