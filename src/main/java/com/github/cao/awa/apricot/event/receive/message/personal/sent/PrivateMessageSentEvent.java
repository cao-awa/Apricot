package com.github.cao.awa.apricot.event.receive.message.personal.sent;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.message.sent.personal.*;
import com.github.cao.awa.apricot.event.receive.message.*;
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
    public void fireAccomplish(EventHandler handler) {
        if (handler instanceof PrivateMessageSentEventHandler privateMessage) {
            privateMessage.onMessageSent(this);
        }
        super.fireAccomplish(handler);
    }
}
