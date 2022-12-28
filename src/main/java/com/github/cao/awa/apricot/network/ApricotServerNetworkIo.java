package com.github.cao.awa.apricot.network;

import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.lazy.*;
import com.github.cao.awa.apricot.server.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.*;

import java.util.concurrent.*;

/**
 * Network io of apricot bot server.
 *
 * @author 草二号机
 * @since 1.0.0
 */
public class ApricotServerNetworkIo {
    public static final Lazy<NioEventLoopGroup> DEFAULT_CHANNEL = new Lazy<>(() -> new NioEventLoopGroup(
            0,
            Executors.newCachedThreadPool()
    ));
    public static final Lazy<EpollEventLoopGroup> EPOLL_CHANNEL = new Lazy<>(() -> new EpollEventLoopGroup(
            0,
            Executors.newCachedThreadPool()
    ));

    private final ApricotServer server;

    public ApricotServerNetworkIo(ApricotServer server) {
        this.server = server;
    }

    public void start(final int port) throws Exception {
        boolean epoll = Epoll.isAvailable();

        Lazy<? extends EventLoopGroup> lazy = epoll ? EPOLL_CHANNEL : DEFAULT_CHANNEL;

        Class<? extends ServerSocketChannel> channel = epoll ?
                                                       EpollServerSocketChannel.class :
                                                       NioServerSocketChannel.class;

        EventLoopGroup boss = lazy.get();
        EventLoopGroup worker = lazy.get();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.channel(channel)
                     .group(
                             boss,
                             worker
                     )
                     .option(
                             ChannelOption.SO_BACKLOG,
                             0
                     )
                     .option(
                             ChannelOption.TCP_NODELAY,
                             true
                     )
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel ch) {
                             ChannelPipeline pipeline = ch.pipeline();
                             pipeline.addLast(new HttpServerCodec());
                             pipeline.addLast(new HttpObjectAggregator(65536));
                             pipeline.addLast(new WebSocketServerCompressionHandler());
                             pipeline.addLast(new WebSocketServerProtocolHandler(
                                     "/",
                                     null,
                                     true
                             ));
                             pipeline.addLast(new ApricotRequestHandler(ApricotServerNetworkIo.this.server));
                         }
                     });
            ChannelFuture future = bootstrap.bind(port)
                                            .sync();
            future.channel()
                  .closeFuture()
                  .sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}