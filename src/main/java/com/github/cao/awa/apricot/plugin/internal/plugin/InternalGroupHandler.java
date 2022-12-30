package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.event.handler.accomplish.message.group.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.group.*;
import org.apache.logging.log4j.*;

public class InternalGroupHandler extends GroupMessageReceivedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("InternalGroup");

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
    public void onMessageReceived(GroupMessageReceivedEvent<?> event) {
        LOGGER.info("GROUP * " + event.getPacket()
                                             .getSender()
                                             .getName() + ": " + event.getPacket()
                                                                      .getMessage()
                                                                      .toPlainText());
    }
}
