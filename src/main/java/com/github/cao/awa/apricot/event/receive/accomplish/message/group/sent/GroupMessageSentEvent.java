package com.github.cao.awa.apricot.event.receive.accomplish.message.group.sent;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.message.sent.group.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.message.group.sent.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.group.sent.*;
import com.github.cao.awa.apricot.network.router.*;

public abstract class GroupMessageSentEvent<T extends GroupMessageSentPacket> extends MessageSentEvent<T> {
    public GroupMessageSentEvent(ApricotProxy proxy, T packet) {
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
        if (handler instanceof GroupMessageSentEventHandler groupMessage) {
            groupMessage.onMessageSent(this);
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
        if (handler instanceof GroupMessageSentFilter receivedFilter) {
            return receivedFilter.legitimate(this) && super.fireFirewall(handler);
        }
        return true;
    }
}
