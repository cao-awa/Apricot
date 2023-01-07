package com.github.cao.awa.apricot.event.receive.accomplish.poke;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.poke.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.poke.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.network.packet.receive.poke.*;
import com.github.cao.awa.apricot.network.router.*;

public abstract class PokeReceivedEvent<T extends PokeReceivedPacket> extends Event<T> {
    public PokeReceivedEvent(ApricotProxy proxy, T packet) {
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
    public void fireAccomplish(AccomplishEventHandler handler) {
        if (handler instanceof PokeReceivedEventHandler poke) {
            poke.onPoke(this);
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
        if (handler instanceof PokeReceivedFilter filter) {
            return filter.legitimate(this);
        }
        return true;
    }
}
