package com.github.cao.awa.apricot.plugin.internal.lawn.handler.message;

import com.github.cao.awa.apricot.database.message.store.*;
import com.github.cao.awa.apricot.event.handler.message.recall.*;
import com.github.cao.awa.apricot.event.receive.message.recall.*;
import com.github.cao.awa.apricot.message.store.*;
import com.github.cao.awa.apricot.network.packet.receive.message.recall.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.task.intensive.*;

public class RecalledMessageHandler extends MessageRecallEventHandler {
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
        store.setRecalled(true);
        messageDatabase.setFromId(packet.getMessageId(), store);
    }

    /**
     * The messages store service is intensive IO.
     *
     * @return Intensive
     */
    @Override
    public IntensiveType intensive() {
        return IntensiveType.IO;
    }
}