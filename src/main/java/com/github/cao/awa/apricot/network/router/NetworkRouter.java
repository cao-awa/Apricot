package com.github.cao.awa.apricot.network.router;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.server.*;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.*;
import org.jetbrains.annotations.*;

@Stable
public abstract class NetworkRouter extends SimpleChannelInboundHandler<WebSocketFrame> {
    private final @NotNull ApricotServer server;

    public final @NotNull ApricotServer getServer() {
        return this.server;
    }

    public NetworkRouter(@NotNull ApricotServer server) {
        this.server = server;
    }
}
