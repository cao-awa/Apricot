package com.github.cao.awa.bot.network.packet.recevied.message;

import com.github.cao.awa.bot.event.receive.message.*;
import com.github.cao.awa.bot.message.*;
import com.github.cao.awa.bot.network.handler.*;
import com.github.cao.awa.bot.network.packet.*;
import com.github.cao.awa.bot.network.packet.recevied.message.sender.*;
import com.github.cao.awa.bot.network.packet.send.message.*;
import com.github.cao.awa.bot.server.*;

import java.util.function.*;

public class MessageReceivedPacket extends ReadonlyPacket {
    private final SendMessageType type;
    private final Message message;
    private final long botId;
    private final MessageSender sender;
    private final long responseId;

    public MessageReceivedPacket(SendMessageType type, Message message, long botId, MessageSender sender, long responseId, long timestamp) {
        this.type = type;
        this.message = message;
        this.botId = botId;
        this.sender = sender;
        this.responseId = responseId;
    }

    public long getBotId() {
        return this.botId;
    }

    public long getResponseId() {
        return this.responseId;
    }

    public Message getMessage() {
        return this.message;
    }

    public SendMessageType getType() {
        return this.type;
    }

    public long getSenderId() {
        return this.sender.getUserId();
    }

    public MessageSender getSender() {
        return sender;
    }

    public void fireEvent(ApricotServer server, ApricotProxy proxy) {
        server.fireEvent(new MessageReceivedEvent(
                proxy,
                this
        ));
    }
}
