package com.github.cao.awa.apricot.event.receive.message;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.message.sent.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.router.*;

public abstract class MessageSentEvent<T extends MessageSentPacket> extends MessageEvent<T> {
    public MessageSentEvent(ApricotProxy proxy, T packet) {
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
        if (handler instanceof MessageSentEventHandler receivedHandler) {
            receivedHandler.onMessageReceived(this);
        }
    }
}

