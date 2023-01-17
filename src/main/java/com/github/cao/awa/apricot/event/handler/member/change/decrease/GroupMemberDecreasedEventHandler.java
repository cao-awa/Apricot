package com.github.cao.awa.apricot.event.handler.member.change.decrease;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.member.change.decrease.*;

public abstract class GroupMemberDecreasedEventHandler extends EventHandler<GroupMemberDecreasedEvent<?>> {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public String getType() {
        return "notice-group-member-decrease";
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
    public abstract void onDecrease(GroupMemberDecreasedEvent<?> event);
}
