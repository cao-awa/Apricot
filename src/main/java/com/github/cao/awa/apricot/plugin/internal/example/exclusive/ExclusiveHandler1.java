package com.github.cao.awa.apricot.plugin.internal.example.exclusive;

import com.github.cao.awa.apricot.event.handler.message.received.group.*;
import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import com.github.cao.awa.apricot.network.packet.receive.message.group.received.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.target.*;

public class ExclusiveHandler1 extends GroupMessageReceivedEventHandler {
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
        ApricotProxy proxy = event.getProxy();
        ApricotServer server = proxy.server();
        GroupMessageReceivedPacket packet = event.getPacket();

        if (packet.getMessage()
                  .toPlainText()
                  .equals("zzz")) {
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
//                              proxy.send(new SendMessagePacket(
//                                      packet.getType(),
//                                      new TextMessageElement("Timeout...").toMessage(),
//                                      packet.getResponseId()
//                              ));
                              System.out.println("Timeout...");
                          }
                  );
        }

        System.out.println("E1 a.");
    }

    @Override
    public void onException(Exception exception) {
        exception.printStackTrace();
    }

    @Override
    public boolean accept(EventTarget target) {
        return target.group() == 252755050;
    }

    @Override
    public void onExclusive(GroupMessageReceivedEvent<?> event) {
        System.out.println("E1 e.");
    }
}