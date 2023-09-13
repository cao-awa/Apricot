package com.github.cao.awa.apricot.event.receive.member.change.decrease.kick;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.member.change.decrease.kick.*;
import com.github.cao.awa.apricot.event.receive.member.change.decrease.*;
import com.github.cao.awa.apricot.network.packet.receive.member.change.decrease.kick.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class GroupMemberKickedEvent extends GroupMemberDecreasedEvent<GroupMemberKickedPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(ApricotCollectionFactor.hashSet(), set -> {
        set.add("notice-group-member-changed");
        set.add("notice-group-member-decrease");
        set.add("notice-group-decrease-kick");
    });

    public GroupMemberKickedEvent(ApricotProxy proxy, GroupMemberKickedPacket packet) {
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
        if (handler instanceof GroupMemberKickedEventHandler eventHandler) {
            if (this.isExclusive()) {
                eventHandler.onExclusive(this);
            } else {
                eventHandler.onKicked(this);
            }
        }
        super.fireEvent(handler);
    }
}
