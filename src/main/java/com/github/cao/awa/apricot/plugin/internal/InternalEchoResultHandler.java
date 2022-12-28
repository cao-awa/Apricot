package com.github.cao.awa.apricot.plugin.internal;

import com.github.cao.awa.apricot.event.handler.response.*;
import com.github.cao.awa.apricot.event.receive.response.*;
import org.apache.logging.log4j.*;

public class InternalEchoResultHandler extends EchoResultEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("InternalEchoHandler");

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
    public void onResult(EchoResultEvent event) {
        LOGGER.info("Message has an result, echo identifier is: {}",
                    event.getPacket()
                         .getIdentifier());
        event.getProxy().getServer().echo(event.getPacket().getIdentifier(), event.getPacket());
    }
}
