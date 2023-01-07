package com.github.cao.awa.apricot.event.receive.accomplish.message;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.message.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.router.*;

public abstract class MessageReceivedEvent<T extends MessageReceivedPacket> extends MessageEvent<T> {
    public MessageReceivedEvent(ApricotProxy proxy, T packet) {
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
        if (handler instanceof MessageReceivedEventHandler receivedHandler) {
            receivedHandler.onMessageReceived(this);
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
        if (handler instanceof MessageReceivedFilter receivedFilter) {
            return receivedFilter.legitimate(this);
        }
        return true;
    }
}
