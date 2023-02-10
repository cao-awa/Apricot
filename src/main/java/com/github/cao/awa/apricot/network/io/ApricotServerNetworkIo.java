package com.github.cao.awa.apricot.network.io;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.network.io.channel.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.thread.pool.*;
import com.github.cao.awa.apricot.util.thread.*;
import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.epoll.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.*;
import io.netty.channel.socket.nio.*;
import org.apache.logging.log4j.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * Network io of apricot bot server.
 *
 * @author 草二号机
 * @author cao_awa
 * @since 1.0.0
 */
@Stable
public class ApricotServerNetworkIo {
    private static final Logger LOGGER = LogManager.getLogger("ApricotNetworkIo");
    private static final Supplier<NioEventLoopGroup> DEFAULT_CHANNEL = () -> new NioEventLoopGroup(
            0,
            ExecutorFactor.intensiveIo()
    );
    private static final Supplier<EpollEventLoopGroup> EPOLL_CHANNEL = () -> new EpollEventLoopGroup(
            0,
            ExecutorFactor.intensiveIo()
    );

    private final ApricotServerChannelInitializer channelInitializer;
    private final ApricotServer server;
    private final List<ChannelFuture> channels = new CopyOnWriteArrayList<>();

    public ApricotServerNetworkIo(ApricotServer server) {
        this.server = server;
        this.channelInitializer = new ApricotServerChannelInitializer(server);
    }

    public void start(final int port) throws Exception {
        ThreadUtil.setName("ApricotNetworkIo");

        boolean expectEpoll = server.useEpoll();
        boolean epoll = Epoll.isAvailable();

        LOGGER.info(expectEpoll ?
                    epoll ?
                    "Apricot network io using Epoll" :
                    "Apricot network io expected Epoll, but Epoll is not available, switch to NIO" :
                    "Apricot network io using NIO");

        Supplier<? extends EventLoopGroup> lazy = epoll ? EPOLL_CHANNEL : DEFAULT_CHANNEL;

        Class<? extends ServerSocketChannel> channel = epoll ?
                                                       EpollServerSocketChannel.class :
                                                       NioServerSocketChannel.class;

        EventLoopGroup boss = lazy.get();
        EventLoopGroup worker = lazy.get();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            this.channels.add(bootstrap.channel(channel)
                                       .group(
                                               boss,
                                               worker
                                       )
                                       .option(
                                               ChannelOption.SO_BACKLOG,
                                               256
                                       )
                                       .option(
                                               // Real-time response is necessary
                                               // Enable TCP no delay to improve response speeds
                                               ChannelOption.TCP_NODELAY,
                                               true
                                       )
                                       .childHandler(this.channelInitializer)
                                       .bind(port)
                                       .syncUninterruptibly()
                                       .channel()
                                       .closeFuture()
                                       .sync());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public void shutdown() {
        for (ChannelFuture channelFuture : this.channels) {
            try {
                channelFuture.channel()
                             .close()
                             .sync();
            } catch (InterruptedException interruptedException) {
                LOGGER.error("Interrupted whilst closing channel");
            }
        }
    }
}