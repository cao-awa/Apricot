package com.github.cao.awa.apricot.network.handler;

import com.github.cao.awa.apricot.network.connection.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.meta.lifecycle.*;
import org.apache.logging.log4j.*;

public class ApricotHandshakeHandler extends RequestHandler {
    private static final Logger LOGGER = LogManager.getLogger("ApricotHandshake");

    public ApricotHandshakeHandler(ApricotUniqueDispenser dispenser) {
        super(dispenser);
    }

    public synchronized void handlePacket(ReadonlyPacket packet) {
        if (packet instanceof ProxyConnectPacket connect) {
            getDispenser().setId(connect.getId());
            getDispenser().setConnectTime(connect.getTimestamp());
            LOGGER.info(
                    "Proxy '{}' login susses, timestamp is '{}'",
                    connect.getId(),
                    connect.getTimestamp()
            );
            getDispenser().setHandler(new ApricotBotRequestHandler(getDispenser()));
            getDispenser().handle(packet);
        } else {
            LOGGER.info("An proxy try to connect, but authenticate failed");
            getDispenser().disconnect("Authenticate failed");
        }
    }
}
