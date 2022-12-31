package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.event.handler.accomplish.lifecycle.disconnect.*;
import com.github.cao.awa.apricot.event.receive.accomplish.lifecycle.*;
import org.apache.logging.log4j.*;

public class ListenBotDied extends ProxyDisconnectEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("TappingGroupApprove");

    /**
     * Process event.
     *
     * @param event
     *         event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void onDisconnect(ProxyDisconnectEvent event) {
        LOGGER.info("Bot '{}' was died(disconnect), connect timestamp is '{}'", event.getPacket().getId(), event.getPacket().getTimestamp());
//        LOGGER.info("Bot '{}' was die(kicked) by '{}'", event.getPacket().getUserId(), event.getPacket().getOperatorId());
    }
}
