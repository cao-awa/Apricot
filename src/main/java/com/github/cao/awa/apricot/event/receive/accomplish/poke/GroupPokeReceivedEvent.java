package com.github.cao.awa.apricot.event.receive.accomplish.poke;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.poke.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.poke.*;
import com.github.cao.awa.apricot.network.packet.receive.poke.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class GroupPokeReceivedEvent extends PokeReceivedEvent<GroupPokeReceivedPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> {
                set.add("notice-poke");
                set.add("notice-poke-group");
            }
    );

    public GroupPokeReceivedEvent(ApricotProxy proxy, GroupPokeReceivedPacket packet) {
        super(
                proxy,
                packet
        );
    }

    @Override
    public final Set<String> pipeline() {
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
        if (handler instanceof GroupPokeReceivedEventHandler messageReceivedHandler) {
            messageReceivedHandler.onPoke(this);
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
        if (handler instanceof GroupPokeReceivedFilter messageFilter) {
            return messageFilter.legitimate(this) && super.fireFirewall(handler);
        }
        return true;
    }
}
