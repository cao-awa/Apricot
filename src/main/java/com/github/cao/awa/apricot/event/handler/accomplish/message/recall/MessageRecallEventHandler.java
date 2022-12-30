package com.github.cao.awa.apricot.event.handler.accomplish.message.recall;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.recall.*;

public abstract class MessageRecallEventHandler extends AccomplishEventHandler {
    @Override
    public final String getType() {
        return "notice-recall";
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
    public abstract void onRecall(MessageRecallEvent<?> event);
}
