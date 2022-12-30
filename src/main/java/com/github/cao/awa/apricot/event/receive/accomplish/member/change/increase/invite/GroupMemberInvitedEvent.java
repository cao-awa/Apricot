package com.github.cao.awa.apricot.event.receive.accomplish.member.change.increase.invite;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.member.change.increase.invite.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.member.change.increase.invite.*;
import com.github.cao.awa.apricot.event.receive.accomplish.member.change.increase.*;
import com.github.cao.awa.apricot.network.packet.recevied.member.change.increase.invite.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class GroupMemberInvitedEvent extends GroupMemberIncreasedEvent<GroupMemberInvitedPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(ApricotCollectionFactor.newHashSet(), set -> {
        set.add("notice-group-member-changed");
        set.add("notice-group-member-increased");
        set.add("notice-group-increase-invite");
    });

    public GroupMemberInvitedEvent(ApricotProxy proxy, GroupMemberInvitedPacket packet) {
        super(
                proxy,
                packet
        );
    }

    @Override
    public Set<String> pipeline() {
        return TARGETS;
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
        if (handler instanceof GroupMemberInvitedEventHandler approvedHandler) {
            approvedHandler.onInvited(this);
        }
        super.fireAccomplish(handler);
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
        if (handler instanceof GroupMemberInvitedEventFilter filter) {
            return filter.legitimate(this) && super.fireFirewall(handler);
        }
        return true;
    }
}
