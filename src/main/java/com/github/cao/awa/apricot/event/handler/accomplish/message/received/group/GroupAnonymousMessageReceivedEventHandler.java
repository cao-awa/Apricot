package com.github.cao.awa.apricot.event.handler.accomplish.message.received.group;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.group.received.*;

public abstract class GroupAnonymousMessageReceivedEventHandler extends AccomplishEventHandler {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public final String getType() {
        return "message-group-anonymous";
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
    public abstract void onMessageReceived(GroupAnonymousMessageReceivedEvent event);
}
