package com.github.cao.awa.bot.network.handler;

import com.github.cao.awa.bot.network.packet.*;

public class ApricotProxy {
    private final ApricotRequestHandler handler;

    public ApricotProxy(ApricotRequestHandler handler) {
        this.handler = handler;
    }

    public void send(Packet packet) {
        this.handler.send(packet);
    }
}
