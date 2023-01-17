package com.github.cao.awa.apricot.plugin.internal.example.exclusive;

import com.github.cao.awa.apricot.event.handler.message.received.personal.*;
import com.github.cao.awa.apricot.event.receive.message.personal.received.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.network.packet.receive.message.personal.received.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;

public class ExclusiveHandler1 extends PrivateMessageReceivedEventHandler {
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
        ApricotServer server = proxy.server();
        PrivateMessageReceivedPacket packet = event.getPacket();

        if (packet.getMessage()
                  .toPlainText()
                  .equals("zzz")) {
            // Normally handling. require the message is "zzz" to test exclusive.
            // Send......
            proxy.send(new SendMessagePacket(
                    MessageType.PRIVATE,
                    new TextMessageElement("zzz...").toMessage(),
                    packet.getResponseId()
            ));

            // Request exclusive the next messages.
            server.getEventManager()
                  .exclusive(
                          // Target, this uniques for everyone.
                          packet.target(),
                          // Request for this handler.
                          this,
                          // Exclusive twice.
                          2,
                          // Timeout 6 seconds.
                          6000,
                          () -> {
                              // Do something when timeout.
                              proxy.send(new SendMessagePacket(
                                      MessageType.PRIVATE,
                                      new TextMessageElement("Timeout...").toMessage(),
                                      packet.getResponseId()
                              ));
                          }
                  );
        }
    }

    @Override
    public void onExclusive(PrivateMessageReceivedEvent<?> event) {
        // Exclusively.
        // Send......
        event.getProxy()
             .send(new SendMessagePacket(
                     MessageType.PRIVATE,
                     new TextMessageElement("zzz").toMessage(),
                     event.getPacket()
                          .getResponseId()
             ));
    }
}