package com.github.cao.awa.apricot.plugin.internal.plugin.handler.exclusive;

import com.github.cao.awa.apricot.event.handler.message.received.group.*;
import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import org.apache.logging.log4j.*;

public class ExclusiveTest extends GroupMessageReceivedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("Exclusive");

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
        //        if (event.getPacket()
        //                 .getMessage()
        //                 .toPlainText()
        //                 .equals("zzzwww")) {
        //            event.getProxy()
        //                 .server()
        //                 .getEventManager()
        //                 .exclusive(
        //                         event.getPacket()
        //                              .target(),
        //                         this
        //                 );
        //        } else {
        //            LOGGER.info("Exclusive: " + event.getPacket()
        //                                             .getMessage()
        //                                             .toPlainText());
        //        }
    }
}
