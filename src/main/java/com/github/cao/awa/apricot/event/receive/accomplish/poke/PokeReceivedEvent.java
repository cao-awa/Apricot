package com.github.cao.awa.apricot.event.receive.accomplish.poke;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.poke.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.poke.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.recevied.poke.*;

public class PokeReceivedEvent extends Event<PokeReceivedPacket> {
    public PokeReceivedEvent(ApricotProxy proxy, PokeReceivedPacket packet) {
        super(proxy, packet);
    }

    @Override
    public String getName() {
        return "notice-poke";
    }

    /**
     * Fire event, let event entrust handler to process self.
     *
     * @param handler handler
     *
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void fireAccomplish(AccomplishEventHandler handler) {
        if (handler instanceof PokeReceivedEventHandler messageReceivedHandler) {
            messageReceivedHandler.onPoke(this);
        }
    }

    /**
     * Fire event, let event entrust handler to process self.
     *
     * @param handler handler
     *
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public boolean fireFirewall(FirewallEventHandler handler) {
        if (handler instanceof PokeReceivedFilter messageFilter) {
            return messageFilter.legitimate(this);
        }
        return true;
    }
}
