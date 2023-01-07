package com.github.cao.awa.apricot.event.handler.accomplish.message.sent.group;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.group.sent.*;

public abstract class GroupNormalMessageSentEventHandler extends AccomplishEventHandler {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public final String getType() {
        return "message-sent-group-normal";
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
    public abstract void onMessageSent(GroupNormalMessageSentEvent event);
}
