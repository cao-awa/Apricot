package com.github.cao.awa.apricot.plugin.internal.plugin.message;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.event.handler.accomplish.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.store.*;

public class MessageReproduce extends MessageReceivedEventHandler {
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

            MessageStore store = MessageStore.fromJSONObject(
                    server,
                    JSONObject.parse(server.getMessagesHeadOffice()
                                           .get(id))
            );

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
    }
}
