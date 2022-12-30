package com.github.cao.awa.apricot.network.router;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class ApricotProxy {
    private final @NotNull ApricotRequestRouter router;
    private final @NotNull ApricotServer server;

    public ApricotProxy(@NotNull ApricotRequestRouter router, @NotNull ApricotServer server) {
        this.router = router;
        this.server = server;
    }

    public void send(WritablePacket packet) {
        this.router.send(packet);
    }

    public void send(WritablePacket packet, Runnable runnable) {
        this.router.send(
                packet,
                runnable
        );
    }

    @NotNull
    public ApricotServer getServer() {
        return server;
    }

    public void send(@NotNull WritablePacket packet, Consumer<EchoResultPacket> echo) {
        this.router.send(
                packet,
                echo
        );
    }

    public void send(@NotNull WritablePacket packet, Consumer<EchoResultPacket> echo, Runnable runnable) {
        this.router.send(
                packet,
                echo,
                runnable
        );
    }

    public void disconnect() {
        this.router.disconnect();
    }
}