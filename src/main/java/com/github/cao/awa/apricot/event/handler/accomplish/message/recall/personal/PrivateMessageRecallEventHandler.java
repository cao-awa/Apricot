package com.github.cao.awa.apricot.event.handler.accomplish.message.recall.personal;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.recall.personal.*;

public abstract class PrivateMessageRecallEventHandler extends AccomplishEventHandler {
    @Override
    public final String getType() {
        return "notice-friend-recall";
    }

    /**
     * Process event.
     *
     * @param event
     *         event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    public abstract void onRecall(PrivateMessageRecallEvent event);
}
