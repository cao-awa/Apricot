package com.github.cao.awa.apricot.network.handler;

import com.github.cao.awa.apricot.network.packet.*;

public class ApricotProxy {
    private final ApricotRequestHandler handler;

    public ApricotProxy(ApricotRequestHandler handler) {
        this.handler = handler;
    }

    public void send(Packet packet) {
        this.handler.send(packet);
    }

    public void send(Packet packet, Runnable runnable) {
        this.handler.send(packet, runnable);
    }
}
