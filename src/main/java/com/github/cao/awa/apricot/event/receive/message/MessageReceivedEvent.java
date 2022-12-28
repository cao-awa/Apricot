package com.github.cao.awa.apricot.event.receive.message;

import com.github.cao.awa.apricot.event.*;
import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;

public class MessageReceivedEvent extends Event {
    private final MessageReceivedPacket packet;

    public MessageReceivedEvent(ApricotProxy handler, MessageReceivedPacket packet) {
        super(handler);
        this.packet = packet;
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
    public void entrust(EventHandler handler) {
        if (handler instanceof MessageReceivedEventHandler messageReceivedHandler) {
            messageReceivedHandler.onMessageReceived(this);
        }
    }

    public MessageReceivedPacket getPacket() {
        return this.packet;
    }
}
