package com.github.cao.awa.apricot.network.io;

import com.github.cao.awa.apricot.network.handler.*;
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
import org.apache.logging.log4j.*;

import java.util.concurrent.*;
import java.util.function.*;

/**
 * Network io of apricot bot server.
 *
 * @author 草二号机
 * @since 1.0.0
 */
public class ApricotServerNetworkIo {
    private static final Logger LOGGER = LogManager.getLogger("ApricotNetworkIo");
    private static final Supplier<NioEventLoopGroup> DEFAULT_CHANNEL = () -> new NioEventLoopGroup(
            0,
            Executors.newCachedThreadPool()
    );
    private static final Supplier<EpollEventLoopGroup> EPOLL_CHANNEL = () -> new EpollEventLoopGroup(
            0,
            Executors.newCachedThreadPool()
    );

    private final ApricotServer server;

    public ApricotServerNetworkIo(ApricotServer server) {
        this.server = server;
    }

    public void start(final int port) throws Exception {
        boolean expectEpoll = server.useEpoll();
        boolean epoll = Epoll.isAvailable();

        LOGGER.info(expectEpoll ?
                    epoll ?
                    "Apricot network io using Epoll" :
                    "Apricot network io expected epoll, but Epoll is not available, switch to NIO" :
                    "Apricot network io using NIO");

        Supplier<? extends EventLoopGroup> lazy = epoll ? EPOLL_CHANNEL : DEFAULT_CHANNEL;

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
                             // Real-time response is necessary
                             // Enable TCP No delay to improve response speeds
                             ChannelOption.TCP_NODELAY,
                             true
                     )
                     .childHandler(new ChannelInitializer<SocketChannel>() {
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
                                     1073741824
                             ));
                             // Do handle
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