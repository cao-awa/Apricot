package com.github.cao.awa.apricot.event.receive.firewall.invalid;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.invalid.*;
import com.github.cao.awa.apricot.event.receive.firewall.*;
import com.github.cao.awa.apricot.network.packet.receive.invalid.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class InvalidDataReceivedEvent extends FirewallEvent<InvalidDataReceivedPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> set.add("invalid-data")
    );


    public InvalidDataReceivedEvent(ApricotProxy proxy, InvalidDataReceivedPacket packet) {
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
    public boolean fireFirewall(FirewallEventHandler handler) {
        if (handler instanceof InvalidDataReceivedHandler messageFilter) {
            return messageFilter.legitimate(this);
        }
        return true;
    }
}
