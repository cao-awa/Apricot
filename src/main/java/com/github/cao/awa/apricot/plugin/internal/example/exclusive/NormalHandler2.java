package com.github.cao.awa.apricot.plugin.internal.example.exclusive;

import com.github.cao.awa.apricot.event.handler.message.received.personal.*;
import com.github.cao.awa.apricot.event.receive.message.personal.received.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.network.packet.receive.message.personal.received.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;

public class NormalHandler2 extends PrivateMessageReceivedEventHandler {
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
        ApricotProxy proxy = event.getProxy();
        PrivateMessageReceivedPacket packet = event.getPacket();

        if (! event.getPacket()
                   .getMessage()
                   .toPlainText()
                   .equals("www")) {
            return;
        }

        proxy.send(new SendMessagePacket(
                MessageType.PRIVATE,
                new TextMessageElement("www?").toMessage(),
                packet.getResponseId()
        ));
    }
}
