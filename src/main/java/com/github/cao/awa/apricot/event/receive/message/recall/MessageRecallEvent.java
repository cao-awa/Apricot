package com.github.cao.awa.apricot.event.receive.message.recall;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.message.recall.*;
import com.github.cao.awa.apricot.event.receive.*;
import com.github.cao.awa.apricot.network.packet.receive.message.recall.*;
import com.github.cao.awa.apricot.network.router.*;

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
    public void fireAccomplish(EventHandler handler) {
        if (handler instanceof MessageRecallEventHandler receivedHandler) {
            receivedHandler.onRecall(this);
        }
    }
}
