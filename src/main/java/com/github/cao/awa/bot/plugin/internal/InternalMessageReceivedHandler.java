package com.github.cao.awa.bot.plugin.internal;

import com.github.cao.awa.bot.event.handler.message.*;
import com.github.cao.awa.bot.event.receive.message.*;
import com.github.cao.awa.bot.message.*;
import com.github.cao.awa.bot.network.packet.send.message.*;
import org.apache.logging.log4j.*;

public class InternalMessageReceivedHandler extends MessageReceivedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("InternalMessageHandler");

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        LOGGER.info(
                "Handler received message from '{}': {}",
                event.getPacket()
                     .getSenderId(),
                event.getPacket()
                     .getMessage().toPlainText()
        );

        if (event.getPacket()
                 .getMessage()
                 .handleMessage(element -> {
                     if (element instanceof TextMessageElement text) {
                         return text.getText()
                                    .equals("awa");
                     }
                     return false;
                 }, 0)) {
            event.getProxy()
                 .send(new SendMessagePacket(
                         SendMessageType.PRIVATE,
                         "awa",
                         event.getPacket()
                              .getResponseId()
                 ));
        }
    }
}
