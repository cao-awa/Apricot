package com.github.cao.awa.apricot.plugin.internal.plugin.handler.message.id;

import com.github.cao.awa.apricot.database.message.store.*;
import com.github.cao.awa.apricot.event.handler.message.received.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.replay.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;

public class QueryMessageId extends MessageReceivedEventHandler {
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
        try {
            ReplyMessageElement at = packet.getMessage()
                                           .get(
                                                   0,
                                                   ReplyMessageElement.class
                                           );
            if (packet.getMessage()
                      .handleMessage(
                              (element) -> {
                                  if (element instanceof TextMessageElement text) {
                                      return text.toPlainText()
                                                 .equals(".id");
                                  }
                                  return false;
                              },
                              1
                      ) || packet.getMessage()
                                 .handleMessage(
                                         (element) -> {
                                             if (element instanceof TextMessageElement text) {
                                                 return text.toPlainText()
                                                            .equals(".id");
                                             }
                                             return false;
                                         },
                                         2
                                 )) {
                try {
                    MessageDatabase messageDatabase = (MessageDatabase) server.getMessagesHeadOffice();

                    AssembledMessage message = new AssembledMessage();
                    message.participate(new ReplyMessageElement(packet.getMessageId()));
                    message.participate(new TextMessageElement("目标消息的id为: " + at.getMessageId() + "(永久id: " + messageDatabase.getConvert(at.getMessageId()) + ")\n"));
                    message.participate(new TextMessageElement("查询语句的id为: " + packet.getMessageId() + "(永久id: " + packet.getOwnId() + ")"));

                    proxy.echo(new SendMessagePacket(
                            packet.getType(),
                            message,
                            packet.getResponseId()
                    ));
                } catch (Exception e) {
                    AssembledMessage message = new AssembledMessage();
                    message.participate(new ReplyMessageElement(packet.getMessageId()));
                    message.participate(new TextMessageElement("发生了一些错误导致无法查到此消息的id, 或许是未被记录"));
                    proxy.echo(new SendMessagePacket(
                            packet.getType(),
                            message,
                            packet.getResponseId()
                    ));
                }
            }
        } catch (Exception e) {
        }
    }
}
