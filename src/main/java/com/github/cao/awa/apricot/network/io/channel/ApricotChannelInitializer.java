package com.github.cao.awa.apricot.network.io.channel;

import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.server.*;
import io.netty.channel.*;
import io.netty.channel.socket.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.*;

/**
 * Channel initializer of apricot network.
 *
 * @author 草二号机
 * @since 1.0.0
 */
public class ApricotChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final ApricotServer server;

    public ApricotChannelInitializer(ApricotServer server) {
        this.server = server;
    }

    /**
     * This method will be called once the {@link Channel} was registered. After the method returns this instance
     * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
     *
     * @param ch
     *         the {@link Channel} which was registered.
     */
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        // Do decodes
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(
                "/",
                null,
                true,
                65536 * 16
        ));
        // Do handle
        pipeline.addLast(new ApricotRequestHandler(this.server));
    }
}
