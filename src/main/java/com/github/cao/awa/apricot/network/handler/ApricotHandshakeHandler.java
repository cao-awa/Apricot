package com.github.cao.awa.apricot.network.handler;

import com.github.cao.awa.apricot.network.dispenser.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.meta.lifecycle.*;
import org.apache.logging.log4j.*;

public class ApricotHandshakeHandler extends RequestHandler {
    private static final Logger LOGGER = LogManager.getLogger("ApricotHandshake");

    public ApricotHandshakeHandler(ApricotUniqueDispenser dispenser) {
        super(dispenser);
    }

    public void handlePacket(ReadonlyPacket packet) {
        synchronized (this) {
            if (packet instanceof ProxyConnectPacket connect) {
                // Process authenticate packet.
                getDispenser().setId(connect.getId());
                getDispenser().setConnectTime(connect.getTimestamp());
                LOGGER.info(
                        "Proxy '{}' login susses, timestamp is '{}'",
                        connect.getId(),
                        connect.getTimestamp()
                );
                // Switch to the request handler
                getDispenser().setHandler(new ApricotBotRequestHandler(getDispenser()));
                // Let events handle authenticate packet
                getDispenser().handle(packet);
            } else {
                if (getDispenser().getHandler() == this) {
                    // Proxy are not done authenticate, disconnect.
                    LOGGER.info("An proxy try to connect, but authenticate failed");
                    getDispenser().disconnect("Authenticate failed");
                } else {
                    // Should not entrust to here, let it go back.
                    getDispenser().handle(packet);
                }
            }
        }
    }
}
