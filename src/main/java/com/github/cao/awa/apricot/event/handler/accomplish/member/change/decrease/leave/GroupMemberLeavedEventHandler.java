package com.github.cao.awa.apricot.event.handler.accomplish.member.change.decrease.leave;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.member.change.decrease.leave.*;

public abstract class GroupMemberLeavedEventHandler extends AccomplishEventHandler {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public String getType() {
        return "notice-group-decrease-leave";
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
    public abstract void onLeaved(GroupMemberLeavedEvent event);
}
