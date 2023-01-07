package com.github.cao.awa.apricot.event.receive.accomplish.lifecycle;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.lifecycle.disconnect.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.lifecycle.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.network.packet.receive.meta.lifecycle.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class ProxyConnectEvent extends Event<ProxyConnectPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> {
                set.add("meta-lifecycle-connect");
            }
    );

    public ProxyConnectEvent(ApricotProxy proxy, ProxyConnectPacket packet) {
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
        if (handler instanceof ProxyConnectEventHandler approvedHandler) {
            approvedHandler.onConnect(this);
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
        if (handler instanceof ProxyConnectFilter filter) {
            return filter.legitimate(this);
        }
        return true;
    }
}
