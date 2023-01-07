package com.github.cao.awa.apricot.plugin.internal.plugin.message;

import com.github.cao.awa.apricot.database.message.store.*;
import com.github.cao.awa.apricot.event.handler.accomplish.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.store.*;
import org.apache.logging.log4j.*;

public class MessageReproduce extends MessageReceivedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("MessageReproducer");

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
    public void onMessageReceived(MessageReceivedEvent<?> event) {
        MessageReceivedPacket packet = event.getPacket();
        ApricotProxy proxy = event.getProxy();
        ApricotServer server = proxy.server();
        String command = packet.getMessage()
                               .toPlainText();
        if (command.startsWith(".")) {
            command = command.substring(1);
        }

        if (command.startsWith("reproduce")) {
            String id = command.substring(command.indexOf(" ") + 1);

            MessageStore store = server.getMessagesHeadOffice()
                                       .get(Long.valueOf(id));

            proxy.send(new SendMessagePacket(
                    packet.getType(),
                    store.getMessage(),
                    packet.getResponseId()
            ));

            if (store.getGroupId() != - 1) {
                proxy.send(new SendMessagePacket(
                        packet.getType(),
                        new TextMessageElement("信息 '" + store.getMessageId() + "' 来自群 '" + store.getGroupId() + "', 发送者是 '" + store.getSenderId() + "'").toMessage(),
                        packet.getResponseId()
                ));
            } else {
                proxy.send(new SendMessagePacket(
                        packet.getType(),
                        new TextMessageElement("信息 '" + store.getMessageId() + "' 来自私聊 '" + store.getSenderId() + "'").toMessage(),
                        packet.getResponseId()
                ));
            }
        }

        if (command.startsWith("recalled")) {
            MessageDatabase messageDatabase = (MessageDatabase) server.getMessagesHeadOffice();
            server.getRelationalDatabase("databases/message/relational/" + Long.parseLong(command.substring(command.indexOf(" ") + 1)) + ".db")
                  .forEach((k, v) -> {
                      MessageStore store = messageDatabase.get(v);
                      if (store != null) {
                          if (store.isRecalled()) {
                              LOGGER.info(
                                      "Recalled message '{}'('{}') is '{}'",
                                      v,
                                      store.getMessageId(),
                                      store.toJSONObject()
                              );
                          }
                      }
                  });

            //            server.getMessagesHeadOffice()
            //                  .forEach((k, v) -> {
            //                      if (v.isRecalled()) {
            //                          LOGGER.info(
            //                                  "Recalled message '{}'('{}') is '{}'",
            //                                  k,
            //                                  v.getMessageId(),
            //                                  v.getMessage()
            //                          );
            //                      }
            //                  });
        }
    }
}
