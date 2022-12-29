package com.github.cao.awa.apricot.network.handler;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.server.*;
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
public class ApricotRequestHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private static final Logger LOGGER = LogManager.getLogger("ApricotRequestHandler");
    private static final Consumer<EchoResultPacket> DO_NO_HANDLE_ECHO = result -> {};
    private final @NotNull ApricotServer server;
    private final @NotNull ApricotProxy proxy;
    private final @NotNull StringBuilder fragment = new StringBuilder();
    private PacketJSONBufWriter writer;
    private Channel channel;

    public ApricotRequestHandler(@NotNull ApricotServer server) {
        this.server = server;
        this.proxy = new ApricotProxy(
                this,
                server
        );
    }

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        super.channelActive(context);
        this.channel = context.channel();
        this.writer = new PacketJSONBufWriter(
                this.server,
                this.channel
        );
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, WebSocketFrame frame) {
        this.server.getTrafficsCounter()
                   .in(frame.content()
                            .array().length);
        this.server.getPacketsCounter()
                   .in(1);
        handleFragment(frame);
    }

    private void handleFragment(WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame textFrame) {
            if (frame.isFinalFragment()) {
                handleRequest(textFrame);
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
                handleRequest(new TextWebSocketFrame(this.fragment.toString()));
                this.fragment.setLength(0);
            }
        } else {
            LOGGER.warn("Occurs unexpected fragment appender received");
        }

    }

    public void handleRequest(TextWebSocketFrame frame) {
       final String text = frame.text();
        this.server.submitTask(() -> {
            final ReadonlyPacket packet = this.server.createPacket(JSONObject.parseObject(text));
            packet.fireEvent(
                    this.server,
                    this.proxy
            );
        });
    }

    public void send(Packet packet) {
        this.server.echo(packet, DO_NO_HANDLE_ECHO);
        packet.writeAndFlush(this.writer);
    }

    public void send(Packet packet, Runnable callback) {
        send(packet);
        callback.run();
    }

    public void send(Packet packet, Consumer<EchoResultPacket> echo) {
        this.server.echo(
                packet,
                echo
        );
        packet.writeAndFlush(this.writer);
    }

    public void send(Packet packet, Consumer<EchoResultPacket> echo, Runnable callback) {
        this.server.echo(
                packet,
                echo
        );
        packet.writeAndFlush(this.writer);
        callback.run();
    }
}