package com.github.cao.awa.apricot.event.handler.add.friend;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.add.friend.*;

public abstract class AddFriendEventHandler extends EventHandler<AddFriendEvent> {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public final String getType() {
        return "request-friend";
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
    public abstract void onAdd(AddFriendEvent event);
}
