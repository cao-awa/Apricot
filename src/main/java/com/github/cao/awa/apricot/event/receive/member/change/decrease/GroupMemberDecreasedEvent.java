package com.github.cao.awa.apricot.event.receive.member.change.decrease;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.member.change.decrease.*;
import com.github.cao.awa.apricot.event.receive.member.change.*;
import com.github.cao.awa.apricot.network.packet.receive.member.change.decrease.*;
import com.github.cao.awa.apricot.network.router.*;

public abstract class GroupMemberDecreasedEvent<T extends GroupMemberDecreasedPacket> extends GroupMemberChangedEvent<T> {
    public GroupMemberDecreasedEvent(ApricotProxy proxy, T packet) {
        super(
                proxy,
                packet
        );
    }

    /**
     * Fire event, let event entrust handler to process self.
     *
     * @param handler
     *         handler
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void fireAccomplish(EventHandler handler) {
        if (handler instanceof GroupMemberDecreasedEventHandler approvedHandler) {
            approvedHandler.onDecrease(this);
        }
    }
}
