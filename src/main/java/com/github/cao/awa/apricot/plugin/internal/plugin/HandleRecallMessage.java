package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.database.message.store.*;
import com.github.cao.awa.apricot.event.handler.accomplish.message.recall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.recall.*;
import com.github.cao.awa.apricot.network.packet.receive.message.recall.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.store.*;

public class HandleRecallMessage extends MessageRecallEventHandler {
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

        MessageDatabase messageDatabase = (MessageDatabase) server.getMessagesHeadOffice();
        MessageStore store = messageDatabase.getFromId(packet.getMessageId());
        store.setRecalled(true);
        messageDatabase.setFromId(packet.getMessageId(), store);
    }
}
