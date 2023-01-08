package com.github.cao.awa.apricot.event.receive.message;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.router.*;

public abstract class MessageEvent<T extends MessagePacket> extends Event<T> {
    public MessageEvent(ApricotProxy proxy, T packet) {
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
        if (handler instanceof MessageEventHandler receivedHandler) {
            receivedHandler.onMessage(this);
        }
    }
}

