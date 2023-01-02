package com.github.cao.awa.apricot.network.handler;

import com.github.cao.awa.apricot.network.dispenser.*;
import com.github.cao.awa.apricot.network.packet.*;

public class ApricotBotRequestHandler extends RequestHandler {
    public ApricotBotRequestHandler(ApricotUniqueDispenser dispenser) {
        super(dispenser);
    }

    @Override
    public void handlePacket(ReadonlyPacket packet) {
        packet.fireEvent(
                getDispenser().getServer(),
                getDispenser().getRouter().getProxy()
        );
    }
}
