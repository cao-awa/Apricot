package com.github.cao.awa.bot.plugin.internal;

import com.github.cao.awa.bot.event.handler.message.*;
import com.github.cao.awa.bot.event.receive.message.*;
import com.github.cao.awa.bot.network.packet.send.message.*;
import org.apache.logging.log4j.*;

public class InternalMessageReceivedHandler extends MessageReceivedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("InternalMessageHandler");

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getPacket()
                 .getMessage().equals("aaawww114514")) {
            LOGGER.info("Message received from {}: {}",
                        event.getPacket().getSenderId(),
                        event.getPacket()
                             .getMessage()
            );
            System.out.println(event.getPacket().getType());
            System.out.println(event.getPacket().getSenderId());
            System.out.println(event.getPacket().getMessage());
            System.out.println(event.getPacket().getResponseId());
            event.getProxy()
                 .send(new SendMessagePacket(
                         event.getPacket().getType(),
                         "www",
                         event.getPacket().getResponseId()
                 ));
        }
    }
}
