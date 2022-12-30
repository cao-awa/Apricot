package com.github.cao.awa.apricot.event.handler.accomplish.message.personal;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.personal.*;

public abstract class PrivateFriendMessageReceivedEventHandler extends AccomplishEventHandler {
    @Override
    public final String getType() {
        return "message-private-friend";
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
    public abstract void onMessageReceived(PrivateFriendMessageReceivedEvent event);
}
