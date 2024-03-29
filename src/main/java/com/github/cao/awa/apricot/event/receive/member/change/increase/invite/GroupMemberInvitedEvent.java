package com.github.cao.awa.apricot.event.receive.member.change.increase.invite;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.member.change.increase.invite.*;
import com.github.cao.awa.apricot.event.receive.member.change.increase.*;
import com.github.cao.awa.apricot.network.packet.receive.member.change.increase.invite.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class GroupMemberInvitedEvent extends GroupMemberIncreasedEvent<GroupMemberInvitedPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(ApricotCollectionFactor.hashSet(), set -> {
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
    public void fireEvent(EventHandler<?> handler) {
        if (handler instanceof GroupMemberInvitedEventHandler eventHandler) {
            if (this.isExclusive()) {
                eventHandler.onExclusive(this);
            } else {
                eventHandler.onInvited(this);
            }
        }
        super.fireEvent(handler);
    }
}
