package com.github.cao.awa.apricot.plugin.internal.plugin.message;

import com.github.cao.awa.apricot.database.message.store.*;
import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.store.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.text.*;
import org.apache.logging.log4j.*;

import java.util.*;

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
        List<String> tokens = TextUtil.splitToList(
                command,
                ' '
        );

        if (tokens.size() > 0) {
            if ("reproduce".equals(tokens.get(0))) {
                MessageStore store;
                if (tokens.size() > 1) {
                    if ("idp".equals(tokens.get(1))) {
                        String id = tokens.get(2);

                        store = server.getMessagesHeadOffice()
                                      .get(Long.valueOf(id));

                    } else if ("idi".equals(tokens.get(1))) {
                        String id = tokens.get(2);

                        store = ((MessageDatabase) server.getMessagesHeadOffice()).getFromId(Integer.parseInt(id));
                    } else {
                        proxy.echo(new SendMessagePacket(
                                packet.getType(),
                                new TextMessageElement("参数不正确, 第二个参数应为 'idp' 或 'idi'").toMessage(),
                                packet.getResponseId()
                        ));
                        return;
                    }

                    AssembledMessage message = new AssembledMessage();

                    if (store.getTargetId() != - 1) {
                        message.participate(new TextMessageElement("信息 '" + store.getMessageId() + "' 来自群 '" + store.getTargetId() + "', 发送者是 '" + store.getSenderId() + "'\n"));
                    } else {
                        message.participate(new TextMessageElement("信息 '" + store.getMessageId() + "' 来自私聊 '" + store.getSenderId() + "'\n"));
                    }

                    message.participate(new TextMessageElement(store.isRecalled() ? "此消息已被撤回, 内容是:\n" : "内容是:\n"));

                    store.getMessage()
                         .forEach(message::participate);

                    proxy.echo(
                            new SendMessagePacket(
                                    packet.getType(),
                                    message,
                                    packet.getResponseId()
                            )
                    );
                }
            }
        }

        if (command.startsWith("recalled")) {
            MessageDatabase messageDatabase = (MessageDatabase) server.getMessagesHeadOffice();
            server.getRelationalDatabase(
                          packet.getBotId(),
                          Long.parseLong(command.substring(command.indexOf(" ") + 1))
                  )
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
