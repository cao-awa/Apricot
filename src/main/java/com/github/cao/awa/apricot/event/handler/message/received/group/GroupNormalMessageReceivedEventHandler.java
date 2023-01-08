package com.github.cao.awa.apricot.event.handler.message.received.group;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.message.group.received.*;

public abstract class GroupNormalMessageReceivedEventHandler extends EventHandler {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public final String getType() {
        return "message-group-normal";
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
    public abstract void onMessageReceived(GroupNormalMessageReceivedEvent event);
}
