package com.github.cao.awa.apricot.event.receive.message.personal.received;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.message.received.personal.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.personal.received.*;
import com.github.cao.awa.apricot.network.router.*;

public abstract class PrivateMessageReceivedEvent<T extends PrivateMessageReceivedPacket> extends MessageReceivedEvent<T> {
    public PrivateMessageReceivedEvent(ApricotProxy proxy, T packet) {
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
    public void fireEvent(EventHandler handler) {
        if (handler instanceof PrivateMessageReceivedEventHandler privateMessage) {
            privateMessage.onMessageReceived(this);
        }
        super.fireEvent(handler);
    }
}
