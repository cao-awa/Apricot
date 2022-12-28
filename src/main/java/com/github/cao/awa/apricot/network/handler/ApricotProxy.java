package com.github.cao.awa.apricot.network.handler;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import com.github.cao.awa.apricot.server.*;

import java.util.function.*;

public class ApricotProxy {
    private final ApricotRequestHandler handler;
    private final ApricotServer server;

    public ApricotProxy(ApricotRequestHandler handler, ApricotServer server) {
        this.handler = handler;
        this.server = server;
    }

    public void send(Packet packet) {
        this.handler.send(packet);
    }

    public void send(Packet packet, Runnable runnable) {
        this.handler.send(
                packet,
                runnable
        );
    }

    public ApricotServer getServer() {
        return server;
    }

    public void send(Packet packet, Consumer<EchoResultPacket> echo) {
        this.handler.send(
                packet,
                echo
        );
    }

    public void send(Packet packet, Consumer<EchoResultPacket> echo, Runnable runnable) {
        this.handler.send(
                packet,
                echo,
                runnable
        );
    }
}
