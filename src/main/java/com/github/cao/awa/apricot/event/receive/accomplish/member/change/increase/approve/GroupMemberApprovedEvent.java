package com.github.cao.awa.apricot.event.receive.accomplish.member.change.increase.approve;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.member.change.increase.approve.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.member.change.increase.approve.*;
import com.github.cao.awa.apricot.event.receive.accomplish.member.change.increase.*;
import com.github.cao.awa.apricot.network.packet.recevied.member.change.increase.approve.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class GroupMemberApprovedEvent extends GroupMemberIncreasedEvent<GroupMemberApprovedPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(ApricotCollectionFactor.newHashSet(), set -> {
        set.add("notice-group-member-changed");
        set.add("notice-group-member-increased");
        set.add("notice-group-increase-approve");
    });

    public GroupMemberApprovedEvent(ApricotProxy proxy, GroupMemberApprovedPacket packet) {
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
        if (handler instanceof GroupMemberApprovedEventHandler approvedHandler) {
            approvedHandler.onApproved(this);
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
        if (handler instanceof GroupMemberApprovedEventFilter filter) {
            return filter.legitimate(this) && super.fireFirewall(handler);
        }
        return true;
    }
}
