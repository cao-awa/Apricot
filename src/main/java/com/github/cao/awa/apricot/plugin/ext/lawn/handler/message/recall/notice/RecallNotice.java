package com.github.cao.awa.apricot.plugin.ext.lawn.handler.message.recall.notice;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.database.message.store.*;
import com.github.cao.awa.apricot.event.handler.message.recall.*;
import com.github.cao.awa.apricot.event.receive.message.recall.*;
import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.message.store.*;
import com.github.cao.awa.apricot.network.packet.receive.message.recall.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.io.*;

import java.io.*;
import java.util.*;

public class RecallNotice extends MessageRecallEventHandler {
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
    public void onRecall(MessageRecallEvent<?> event) {
        MessageRecallPacket packet = event.getPacket();
        ApricotProxy proxy = event.getProxy();
        ApricotServer server = proxy.server();

        MessageDatabase messageDatabase = server.getMessagesHeadOffice();
        MessageStore store = messageDatabase.getFromId(packet.getMessageId());
        messageDatabase.setFromId(
                packet.getMessageId(),
                store
        );

        AssembledMessage message = new AssembledMessage();

        message.participate(new TextMessageElement("用户 '" + store.getSenderId() + "' 撤回了消息 '" + store.getMessageId() + "'(永久id: '" + messageDatabase.getConvert(store.getMessageId()) + "')\n内容为:\n"));

        store.getMessage()
             .forEach(message::participate);

        proxy.echo(new SendMessagePacket(
                packet.getType(),
                message,
                packet.getResponseId()
        ));
    }

    @Override
    public boolean accept(EventTarget target) {
        try {
            List<Long> list = JSONObject.parse(IOUtil.read(new FileInputStream("configs/plugins/internal/recall/reject/whitelist.json")))
                                        .getJSONArray("lists")
                                        .toList(Long.TYPE);
            if (target.group() != - 1) {
                return list.contains(target.group());
            }
            return list.contains(target.person());
        } catch (Exception e) {
            return false;
        }
    }
}
