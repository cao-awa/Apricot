package com.github.cao.awa.apricot.plugin.internal;

import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import org.apache.logging.log4j.*;

public class InternalMessageReceivedHandler extends MessageReceivedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("InternalMessageHandler");

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
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getPacket()
                 .getType() == SendMessageType.PRIVATE && event.getPacket()
                                                               .getMessage()
                                                               .handleMessage(
                                                                       element -> {
                                                                           if (element instanceof TextMessageElement text) {
                                                                               return text.getText()
                                                                                          .equals("awa");
                                                                           }
                                                                           return false;
                                                                       },
                                                                       0
                                                               )) {
            LOGGER.info(
                    "Handling 'awa' from {}, responding 'awa'",
                    event.getPacket()
                         .getSenderId()
            );
            event.getProxy()
                 .send(new SendMessagePacket(
                         SendMessageType.PRIVATE,
                         "awa",
                         event.getPacket()
                              .getResponseId()
                 ), echo -> {
                     System.out.println(echo.getIdentifier());
                 });
        }
    }
}
