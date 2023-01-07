package com.github.cao.awa.apricot.event.receive.accomplish.add.group;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.add.group.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.add.group.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.network.packet.receive.add.group.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class InviteGroupEvent extends Event<InviteGroupReceivedPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> set.add("request-group-invite")
    );

    public InviteGroupEvent(ApricotProxy proxy, InviteGroupReceivedPacket packet) {
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
        if (handler instanceof InviteGroupEventHandler eventHandler) {
            eventHandler.onInvite(this);
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
        if (handler instanceof InviteGroupFilter filter) {
            return filter.legitimate(this);
        }
        return true;
    }
}
