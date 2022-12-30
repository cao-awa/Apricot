package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.event.handler.accomplish.message.recall.personal.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.recall.personal.*;

public class RecallNotice extends PrivateMessageRecallEventHandler {
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
    public void onRecall(PrivateMessageRecallEvent event) {
        System.out.println(event.getPacket().getMessageId());
    }
}
