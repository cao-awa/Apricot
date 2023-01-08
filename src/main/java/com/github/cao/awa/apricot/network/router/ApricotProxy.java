package com.github.cao.awa.apricot.network.router;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public record ApricotProxy(@NotNull ApricotRouter router, @NotNull ApricotServer server) {
    public ApricotProxy(@NotNull ApricotRouter router, @NotNull ApricotServer server) {
        this.router = router;
        this.server = server;
    }

    public void echo(WritablePacket<? extends ResponsePacket> packet) {
        this.router.echo(packet);
    }

    public void echo(WritablePacket<? extends ResponsePacket> packet, Runnable runnable) {
        this.router.echo(
                packet,
                runnable
        );
    }

    public void echo(@NotNull WritablePacket<? extends ResponsePacket> packet, Consumer<EchoResultPacket> echo) {
        this.router.echo(
                packet,
                echo
        );
    }

    public void echo(@NotNull WritablePacket<? extends ResponsePacket> packet, Consumer<EchoResultPacket> echo, Runnable runnable) {
        this.router.echo(
                packet,
                echo,
                runnable
        );
    }

    public <R extends ResponsePacket, T extends WritablePacket<R>> void send(T packet, Consumer<R> result) {
        this.router.send(
                packet,
                result
        );
    }

    public <R extends ResponsePacket, T extends WritablePacket<R>> void send(T packet) {
        this.router.send(packet);
    }

    public void disconnect() {
        this.router.disconnect();
    }
}
