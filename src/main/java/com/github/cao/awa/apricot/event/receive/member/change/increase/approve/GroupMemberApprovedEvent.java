package com.github.cao.awa.apricot.event.receive.member.change.increase.approve;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.member.change.increase.approve.*;
import com.github.cao.awa.apricot.event.receive.member.change.increase.*;
import com.github.cao.awa.apricot.network.packet.receive.member.change.increase.approve.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
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
    public void fireEvent(EventHandler handler) {
        if (handler instanceof GroupMemberApprovedEventHandler approvedHandler) {
            approvedHandler.onApproved(this);
        }
        super.fireEvent(handler);
    }
}
