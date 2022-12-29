package com.github.cao.awa.apricot.event.receive.accomplish.message;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.message.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.group.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;

public class MessageReceivedEvent extends Event<MessageReceivedPacket> {
    public MessageReceivedEvent(ApricotProxy handler, MessageReceivedPacket packet) {
        super(handler, packet);
    }

    @Override
    public String getName() {
        return "message-received";
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
        if (handler instanceof MessageReceivedEventHandler messageReceivedHandler) {
            messageReceivedHandler.onMessageReceived(this);
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
        if (handler instanceof MessageIncludeFilter messageFilter) {
            return messageFilter.legitimate(this);
        } else if (handler instanceof GroupFilter groupFilter) {
            return groupFilter.legitimate(this);
        }
        return true;
    }
}
