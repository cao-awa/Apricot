package com.github.cao.awa.bot.event.receive.message;

import com.github.cao.awa.bot.event.*;
import com.github.cao.awa.bot.event.handler.*;
import com.github.cao.awa.bot.event.handler.message.*;
import com.github.cao.awa.bot.network.handler.*;
import com.github.cao.awa.bot.network.packet.recevied.message.*;

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
