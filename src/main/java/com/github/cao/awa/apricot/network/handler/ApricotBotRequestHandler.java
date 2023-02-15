package com.github.cao.awa.apricot.network.handler;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.network.dispenser.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.meta.lifecycle.ProxyDisconnectPacket;

@Stable
public class ApricotBotRequestHandler extends RequestHandler {
    public ApricotBotRequestHandler(ApricotUniqueDispenser dispenser) {
        super(dispenser);
    }

    @Override
    public void handlePacket(ReadonlyPacket packet) {
        if (packet instanceof ProxyDisconnectPacket disconnectPacket) {
            getDispenser().disconnect(disconnectPacket.getDisconnectReason());
        } else {
            packet.fireEvent(
                    getDispenser().getServer(),
                    getDispenser().getRouter()
                                  .getProxy()
            );
        }
    }
}
