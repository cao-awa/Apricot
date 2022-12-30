package com.github.cao.awa.apricot.event.receive.accomplish;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.router.*;

public abstract class NoFirewallEvent extends Event<ReadonlyPacket> {
    public NoFirewallEvent(ApricotProxy proxy, ReadonlyPacket packet) {
        super(
                proxy,
                packet
        );
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
        return true;
    }
}
