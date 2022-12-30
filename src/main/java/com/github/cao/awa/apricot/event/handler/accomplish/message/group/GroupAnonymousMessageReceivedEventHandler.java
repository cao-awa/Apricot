package com.github.cao.awa.apricot.event.handler.accomplish.message.group;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.group.*;

public abstract class GroupAnonymousMessageReceivedEventHandler extends AccomplishEventHandler {
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
