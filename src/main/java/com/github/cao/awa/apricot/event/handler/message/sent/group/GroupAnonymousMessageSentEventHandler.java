package com.github.cao.awa.apricot.event.handler.message.sent.group;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.message.group.sent.*;

public abstract class GroupAnonymousMessageSentEventHandler extends EventHandler<GroupAnonymousMessageSentEvent> {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public final String getType() {
        return "message-sent-group-anonymous";
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
    public abstract void onMessageSent(GroupAnonymousMessageSentEvent event);
}
