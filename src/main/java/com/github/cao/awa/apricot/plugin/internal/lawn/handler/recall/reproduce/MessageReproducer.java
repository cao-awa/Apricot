package com.github.cao.awa.apricot.plugin.internal.lawn.handler.recall.reproduce;

import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.cq.replay.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.message.store.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;

public class MessageReproducer extends MessageEventHandler {
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
    public void onMessage(MessageEvent<?> event) {
        MessagePacket packet = event.getPacket();
        AssembledMessage message = packet.getMessage();
        ApricotProxy proxy = event.getProxy();
        ApricotServer server = proxy.server();

        String plain = message.toPlainText();

        if (plain.startsWith(".reproduce")) {
            AssembledMessage response = new AssembledMessage();
            if (packet.getType() == MessageType.GROUP) {
                response.participate(new ReplyMessageElement(packet.getMessageId()));
            }

            String arg = plain.substring(".reproduce".length() + 1)
                              .trim()
                              .strip();

            try {
                MessageStore store = server.getMessagesHeadOffice()
                                           .get(Long.parseLong(arg));
                if (store == null) {
                    store = server.getMessagesHeadOffice()
                                  .getFromId(Integer.parseInt(arg));
                }

                //                System.out.println("'" + arg + "'来自" + (store.getTargetId() == store.getSenderId() ?
                //                                                          "私聊" :
                //                                                          "群") + "：" + store.getTargetId() + "\n由" + store.getSenderId() + "发送\n内容是：\n" + store.getMessage());
                response.participate(new TextMessageElement("\"" + arg + "\"来自" + (store.getTargetId() == store.getSenderId() ?
                                                                                   "私聊" :
                                                                                   "群") + "：" + store.getTargetId() + "\n由" + store.getSenderId() + "发送" + (
                                                                    store.isRecalled() ?
                                                                    "，已被撤回" :
                                                                    "") + "\n内容是：\n"));
                store.getMessage()
                     .forEach(response::participate);
            } catch (Exception e) {
                response.participate(new TextMessageElement("无法找到消息： '" + arg + "'"));
            }
            proxy.send(new SendMessagePacket(
                    packet.getType(),
                    response,
                    packet.getResponseId()
            ));
        }
    }
}
