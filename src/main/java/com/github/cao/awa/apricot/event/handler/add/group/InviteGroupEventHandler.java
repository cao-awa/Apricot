package com.github.cao.awa.apricot.event.handler.add.group;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.add.group.*;

public abstract class InviteGroupEventHandler extends EventHandler<InviteGroupEvent> {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public String getType() {
        return "request-group-invite";
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
    public abstract void onInvite(InviteGroupEvent event);
}
