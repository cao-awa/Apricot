package com.github.cao.awa.apricot.event.receive.accomplish.message.group;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.message.group.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.message.group.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.group.*;
import com.github.cao.awa.apricot.network.router.*;

public abstract class GroupMessageReceivedEvent<T extends GroupMessageReceivedPacket> extends MessageReceivedEvent<T> {
    public GroupMessageReceivedEvent(ApricotProxy proxy, T packet) {
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
        if (handler instanceof GroupMessageReceivedEventHandler groupMessage) {
            groupMessage.onMessageReceived(this);
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
        if (handler instanceof GroupMessageReceivedFilter receivedFilter) {
            return receivedFilter.legitimate(this) && super.fireFirewall(handler);
        }
        return true;
    }
}
