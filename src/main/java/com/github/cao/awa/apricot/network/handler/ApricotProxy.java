package com.github.cao.awa.apricot.network.handler;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class ApricotProxy {
    private final @NotNull ApricotRequestHandler handler;
    private final @NotNull ApricotServer server;

    public ApricotProxy(@NotNull ApricotRequestHandler handler, @NotNull ApricotServer server) {
        this.handler = handler;
        this.server = server;
    }

    public void send(WritablePacket packet) {
        this.handler.send(packet);
    }

    public void send(WritablePacket packet, Runnable runnable) {
        this.handler.send(
                packet,
                runnable
        );
    }

    @NotNull
    public ApricotServer getServer() {
        return server;
    }

    public void send(@NotNull WritablePacket packet, Consumer<EchoResultPacket> echo) {
        this.handler.send(
                packet,
                echo
        );
    }

    public void send(@NotNull WritablePacket packet, Consumer<EchoResultPacket> echo, Runnable runnable) {
        this.handler.send(
                packet,
                echo,
                runnable
        );
    }
}
