package com.github.cao.awa.apricot.network.handler;

import com.github.cao.awa.apricot.network.dispenser.*;
import com.github.cao.awa.apricot.network.packet.*;

public abstract class RequestHandler {
    private final ApricotUniqueDispenser dispenser;

    public RequestHandler(ApricotUniqueDispenser dispenser) {
        this.dispenser = dispenser;
    }

    public ApricotUniqueDispenser getDispenser() {
        return this.dispenser;
    }

    public abstract void handlePacket(ReadonlyPacket packet);
}