package com.github.cao.awa.apricot.event.receive.firewall.invalid;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.invalid.*;
import com.github.cao.awa.apricot.event.receive.firewall.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.recevied.invalid.*;

public class InvalidDataReceivedEvent extends FirewallEvent<InvalidDataReceivedPacket> {
    public InvalidDataReceivedEvent(ApricotProxy proxy, InvalidDataReceivedPacket packet) {
        super(proxy, packet);
    }

    @Override
    public String getName() {
        return "invalid-data";
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
        if (handler instanceof InvalidDataReceivedHandler messageFilter) {
            return messageFilter.legitimate(this);
        }
        return true;
    }
}
