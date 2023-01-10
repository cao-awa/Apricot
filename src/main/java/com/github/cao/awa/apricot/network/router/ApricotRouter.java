package com.github.cao.awa.apricot.network.router;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.dispenser.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
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
public class ApricotRouter extends NetworkRouter {
    private static final Logger LOGGER = LogManager.getLogger("ApricotRouter");
    private final @NotNull ApricotServer server;
    private final @NotNull ApricotProxy proxy;
    private final @NotNull StringBuilder stitching = new StringBuilder();
    private final ApricotUniqueDispenser dispenser;
    //    private final Set<ReadonlyPacket> broadcasts = ApricotCollectionFactor.newHashSet();
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

    /**
     * Is called for each message of type {@link WebSocketFrame}.
     *
     * @param ctx
     *         the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *         belongs to
     * @param frame
     *         the message to handle
     * @throws Exception
     *         is thrown if an error occurred
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        this.server.getTrafficsCounter()
                   .in(frame.content()
                            .writerIndex());
        this.server.getPacketsCounter()
                   .in(1);
        handleFragment(frame);
    }

    /**
     * Handle fragments and handle the final frame.
     *
     * @param frame
     *         the message
     * @author cao_awa
     * @since 1.0.0
     */
    private void handleFragment(WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame textFrame) {
            // Handle final or not finals fragment happens.
            if (frame.isFinalFragment()) {
                // Final fragment should be direct handle.
                handleFrame(textFrame);
                LOGGER.debug("Handled single fragment packet");

                // Aftermath for wrongly append fragment.
                // In normally, this is redundancy plan, do not wish it be happens.
                if (this.stitching.length() > 0) {
                    // Handle the wrong frame forcefully.
                    LOGGER.debug("Aftermath for wrongly append fragment");
                    handleFrame(new TextWebSocketFrame(this.stitching.toString()));
                    // Let stitching clear.
                    this.stitching.setLength(0);
                }
            } else {
                // Not final fragment should be stitching to one.
                // Usually the fragment stitching length must 0, else then is a wrong.
                if (this.stitching.length() == 0) {
                    // Append this fragment.
                    this.stitching.append(textFrame.text());
                    LOGGER.debug("Handling multi fragment packet");
                } else {
                    LOGGER.warn("Occurs unexpected fragment appended");
                }
            }
        } else if (frame instanceof ContinuationWebSocketFrame continuationFrame) {
            // Handle the continuation fragments.
            this.stitching.append(continuationFrame.text());
            LOGGER.debug("Fragment appended");

            // Let it build to a completed fragment when continuation frame is final.
            if (continuationFrame.isFinalFragment()) {
                // Handle the completed frame.
                LOGGER.debug("Fragment appends done");
                handleFrame(new TextWebSocketFrame(this.stitching.toString()));
                // Let stitching clear.
                this.stitching.setLength(0);
            }
        } else {
            LOGGER.warn("Occurs unexpected fragment appender received");
        }
    }

    /**
     * Handle the frame, frame should be final fragment.
     *
     * @param frame
     *         the message
     * @author cao_awa
     * @since 1.0.0
     */
    private void handleFrame(TextWebSocketFrame frame) {
        handleRequest(JSONObject.parseObject(frame.text()));
    }

    /**
     * Handle the json format message to packet form and handle it.
     *
     * @param request
     *         the request
     * @author 草二号机
     * @author cao_awa
     * @since 1.0.0
     */
    public void handleRequest(JSONObject request) {
        this.server.execute(
                "ApricotRouter",
                () -> handleRequest(this.server.createPacket(request))
        );
    }

    /**
     * Handle the packet.
     *
     * @param request
     *         the request
     * @author cao_awa
     * @since 1.0.0
     */
    public void handleRequest(ReadonlyPacket request) {
        this.dispenser.handle(request);
    }

    /**
     * Broadcast packet for repeat handle.
     *
     * @param packet
     *         the packet
     * @author 草二号机
     * @since 1.0.0
     */
    public void broadcast(ReadonlyPacket packet) {
        handleRequest(packet);
    }

    public void echo(WritablePacket<? extends ResponsePacket> packet, Runnable callback) {
        this.dispenser.echo(
                packet,
                callback
        );
    }

    public void echo(WritablePacket<? extends ResponsePacket> packet) {
        this.dispenser.echo(packet);
    }

    public void echo(WritablePacket<? extends ResponsePacket> packet, Consumer<EchoResultPacket> echo) {
        this.dispenser.echo(
                packet,
                echo
        );
    }

    public void echo(WritablePacket<? extends ResponsePacket> packet, Consumer<EchoResultPacket> echo, Runnable callback) {
        this.dispenser.echo(
                packet,
                echo,
                callback
        );
    }

    public <R extends ResponsePacket, T extends WritablePacket<R>> void send(T packet, Consumer<R> result) {
        this.dispenser.send(
                packet,
                result
        );
    }

    public <R extends ResponsePacket, T extends WritablePacket<R>> void send(T packet) {
        this.dispenser.send(packet);
    }

    /**
     * Disconnect.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    public void disconnect() {
        this.dispenser.disconnect();
    }

    /**
     * Disconnect with reason.
     *
     * @param reason
     *         disconnect reason
     * @author cao_awa
     * @since 1.0.0
     */
    public void disconnect(String reason) {
        this.dispenser.disconnect(reason);
    }

    public Channel getChannel() {
        return this.channel;
    }
}