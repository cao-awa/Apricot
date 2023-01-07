package com.github.cao.awa.apricot.event.receive.accomplish.mute.lift;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.mute.lift.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.mute.lift.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.network.packet.receive.mute.lift.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public abstract class LiftGroupMuteEvent<T extends LiftGroupMuteReceivedPacket> extends Event<T> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> set.add("notice-group-lift-ban")
    );

    public LiftGroupMuteEvent(ApricotProxy proxy, T packet) {
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
        if (handler instanceof LiftGroupMuteEventHandler eventHandler) {
            eventHandler.onLift(this);
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
        if (handler instanceof LiftGroupMuteFilter filter) {
            return filter.legitimate(this);
        }
        return true;
    }
}
