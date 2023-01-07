package com.github.cao.awa.apricot.event.receive.accomplish.member.change;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.member.change.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.member.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.network.packet.receive.member.change.*;
import com.github.cao.awa.apricot.network.router.*;

public abstract class GroupMemberChangedEvent<T extends GroupMemberChangedPacket> extends Event<T> {
    public GroupMemberChangedEvent(ApricotProxy proxy, T packet) {
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
    public void fireAccomplish(AccomplishEventHandler handler) {
        if (handler instanceof GroupMemberChangedEventHandler approvedHandler) {
            approvedHandler.onApproved(this);
        }
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
    public boolean fireFirewall(FirewallEventHandler handler) {
        if (handler instanceof GroupMemberChangedEventFilter filter) {
            return filter.legitimate(this);
        }
        return true;
    }
}
