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
    private final ApricotServer server;
    private final ApricotProxy proxy;
    private PacketJSONBufWriter writer;
    private Channel channel;
    private final @NotNull StringBuilder fragment = new StringBuilder();

    public ApricotRequestHandler(ApricotServer server) {
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
    protected void messageReceived(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        this.server.getTrafficsCounter()
                   .in(frame.content()
                            .array().length);
        this.server.getPacketsCounter()
                   .in(1);
        if (frame instanceof TextWebSocketFrame textFrame) {
            if (frame.isFinalFragment()) {
                if (this.fragment.length() > 0) {
                    handleRequest(new TextWebSocketFrame(this.fragment.toString()));
                    this.fragment.setLength(0);
                }
                handleRequest(textFrame);
            } else {
                if (this.fragment.length() == 0) {
                    this.fragment.append(textFrame.text());
                } else {
                    LOGGER.warn("Occurs unexpected fragment appended");
                }
            }
        } else {
            if (frame instanceof ContinuationWebSocketFrame continuationFrame) {
                this.fragment.append(continuationFrame.text());
                LOGGER.debug("Fragment appended");
            } else {
            }
        }
    }

    public void handleRequest(TextWebSocketFrame frame) {
        String text = frame.text();
        this.server.submitTask(() -> {
            JSONObject request = JSONObject.parseObject(text);
            ReadonlyPacket packet = this.server.createPacket(request);
            if (packet != null) {
                packet.fireEvent(
                        this.server,
                        this.proxy
                );
            }
        });
    }

    public void send(Packet packet) {
        packet.writeAndFlush(this.writer);
    }

    public void send(Packet packet, Runnable callback) {
        packet.writeAndFlush(this.writer);
        callback.run();
    }

    public void send(Packet packet, Consumer<EchoResultPacket> echo) {
        packet.writeAndFlush(this.writer);
        this.server.echo(
                packet,
                echo
        );
    }

    public void send(Packet packet, Consumer<EchoResultPacket> echo, Runnable callback) {
        packet.writeAndFlush(this.writer);
        this.server.echo(
                packet,
                echo
        );
        callback.run();
    }
}