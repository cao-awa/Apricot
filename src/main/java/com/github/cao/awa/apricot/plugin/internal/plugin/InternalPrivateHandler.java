package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.event.handler.accomplish.message.personal.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.personal.*;
import org.apache.logging.log4j.*;

import java.util.*;

public class InternalPrivateHandler extends PrivateMessageReceivedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("InternalPrivate");

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
    public void onMessageReceived(PrivateMessageReceivedEvent<?> event) {
        LOGGER.info("PRIVATE * " + event.getPacket()
                                             .getSender()
                                             .getName() + ": " + event.getPacket()
                                                                      .getMessage()
                                                                      .toPlainText());
    }
}
