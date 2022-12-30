package com.github.cao.awa.apricot.event.receive.accomplish.message.recall;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.message.recall.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.message.recall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.recall.*;

public abstract class MessageRecallEvent<T extends MessageRecallPacket> extends Event<T> {
    public MessageRecallEvent(ApricotProxy proxy, T packet) {
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
        if (handler instanceof MessageRecallEventHandler receivedHandler) {
            receivedHandler.onRecall(this);
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
        if (handler instanceof MessageRecallFilter receivedFilter) {
            return receivedFilter.legitimate(this);
        }
        return true;
    }
}
