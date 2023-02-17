package com.github.cao.awa.apricot.network.router;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.debug.monitor.Monitor;
import com.github.cao.awa.apricot.network.dispenser.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.time.TimeUtil;
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
@Stable
public class ApricotRouter extends NetworkRouter {
    private static final Logger LOGGER = LogManager.getLogger("ApricotRouter");
    private final @NotNull ApricotProxy proxy;
    private final @NotNull StringBuilder stitching = new StringBuilder();
    private final ApricotUniqueDispenser dispenser;
    private ChannelHandlerContext context;
    private Channel channel;
    private int packetCounter = 0;
    private long times = TimeUtil.millions();
    private static final int PACKET_LIMIT_PER_SECONDS = 1000;
    private boolean alive = true;

    public ApricotRouter(@NotNull ApricotServer server) {
        super(server);
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
     * @param ctx   the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *              belongs to
     * @param frame the message to handle
     * @throws Exception is thrown if an error occurred
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (! this.alive) {
            return;
        }
        this.packetCounter++;
        if (this.packetCounter > PACKET_LIMIT_PER_SECONDS) {
            if (TimeUtil.processMillion(this.times) < 1000) {
                LOGGER.warn("Server are received packets more than {} in a seconds, this maybe is a DDOS, attention to attack again",
                            PACKET_LIMIT_PER_SECONDS
                );
                this.disconnect("The server is being attacked by you.");
                this.alive = false;
            } else {
                this.packetCounter = 0;
                this.times = TimeUtil.millions();
            }
            return;
        }
        handleFragment(frame);
    }

    /**
     * Handle fragments and handle the final frame.
     *
     * @param frame the message
     * @author cao_awa
     * @since 1.0.0
     */
    private void handleFragment(WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame textFrame) {
            // Handle final or not finals fragment happens.
            if (frame.isFinalFragment()) {
                // Final fragment should be direct handle.
                handleFrame(textFrame.text());

                // Aftermath for wrongly append fragment.
                // In normally, this is redundancy plan, do not wish it be happens.
                if (this.stitching.length() > 0) {
                    // Handle the wrong frame forcefully.
                    LOGGER.debug("Aftermath for wrongly append fragment");
                    handleFrame(this.stitching.toString());
                    // Let stitching be clear.
                    this.stitching.setLength(0);
                }
            } else {
                // Not final fragment should be stitching to one.
                // Usually the fragment stitching length must 0, else then mean it is a wrong.
                if (this.stitching.length() == 0) {
                    // Append this fragment.
                    this.stitching.append(textFrame.text());
                } else {
                    // Do not handle it if wrongly.
                    LOGGER.warn("Occurs unexpected fragment appends");
                }
            }
        } else if (frame instanceof ContinuationWebSocketFrame continuationFrame) {
            // Handle the continuation fragments.
            this.stitching.append(continuationFrame.text());

            // Let it build to a completed fragment when continuation frame is final.
            if (continuationFrame.isFinalFragment()) {
                // Handle the completed frame.
                handleFrame(this.stitching.toString());
                // Let stitching be clear.
                this.stitching.setLength(0);
            }
        } else {
            // The frame is unsupported, do not process this.
            LOGGER.warn("Occurs unexpected fragment appender received");
            this.stitching.setLength(0);
        }
    }

    /**
     * Handle the frame, frame should be final fragment.
     *
     * @param content the message
     * @author cao_awa
     * @since 1.0.0
     */
    private void handleFrame(String content) {
        handleRequest(JSONObject.parseObject(content));
    }

    /**
     * Handle the json format message to packet form and handle it.
     *
     * @param request the request
     * @author 草二号机
     * @author cao_awa
     * @since 1.0.0
     */
    public void handleRequest(JSONObject request) {
        getServer()
                .intensiveIo()
                .execute(() ->
                                 handleRequest(createPacket(request))
                );
    }

    private ReadonlyPacket createPacket(JSONObject request) {
        return getServer().createPacket(request);
    }

    /**
     * Handle the packet.
     *
     * @param request the request
     * @author cao_awa
     * @since 1.0.0
     */
    public void handleRequest(ReadonlyPacket request) {
        this.dispenser.handle(request);
    }

    /**
     * Disconnect with reason.
     *
     * @param reason disconnect reason
     * @author cao_awa
     * @since 1.0.0
     */
    public void disconnect(String reason) {
        this.dispenser.disconnect(reason);
    }

    /**
     * Broadcast packet for repeat handle.
     *
     * @param packet the packet
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

    public Channel getChannel() {
        return this.channel;
    }
}