package com.github.cao.awa.apricot.event.receive.accomplish.message.personal.sent;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.message.sent.personal.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.message.personal.sent.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.personal.sent.*;
import com.github.cao.awa.apricot.network.router.*;

public abstract class PrivateMessageSentEvent<T extends PrivateMessageSentPacket> extends MessageSentEvent<T> {
    public PrivateMessageSentEvent(ApricotProxy proxy, T packet) {
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
        if (handler instanceof PrivateMessageSentEventHandler privateMessage) {
            privateMessage.onMessageSent(this);
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
        if (handler instanceof PrivateMessageSentFilter receivedFilter) {
            return receivedFilter.legitimate(this) && super.fireFirewall(handler);
        }
        return true;
    }
}
