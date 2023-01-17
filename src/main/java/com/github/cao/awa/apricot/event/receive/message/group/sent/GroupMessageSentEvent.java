package com.github.cao.awa.apricot.event.receive.message.group.sent;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.message.sent.group.*;
import com.github.cao.awa.apricot.event.receive.message.*;
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
    public void fireEvent(EventHandler<?> handler) {
        if (handler instanceof GroupMessageSentEventHandler eventHandler) {
            if (this.isExclusive()) {
                eventHandler.onExclusive(this);
            } else {
                eventHandler.onMessageSent(this);
            }
        }
        super.fireEvent(handler);
    }
}
