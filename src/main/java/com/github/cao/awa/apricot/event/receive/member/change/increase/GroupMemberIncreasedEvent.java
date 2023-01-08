package com.github.cao.awa.apricot.event.receive.member.change.increase;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.member.change.increase.*;
import com.github.cao.awa.apricot.event.receive.member.change.*;
import com.github.cao.awa.apricot.network.packet.receive.member.change.increase.*;
import com.github.cao.awa.apricot.network.router.*;

public abstract class GroupMemberIncreasedEvent<T extends GroupMemberIncreasedPacket> extends GroupMemberChangedEvent<T> {
    public GroupMemberIncreasedEvent(ApricotProxy proxy, T packet) {
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
    public void fireEvent(EventHandler handler) {
        if (handler instanceof GroupMemberIncreasedEventHandler approvedHandler) {
            approvedHandler.onIncrease(this);
        }
        super.fireEvent(handler);
    }
}
