package com.github.cao.awa.bot.network.packet.recevied.message;

import com.github.cao.awa.bot.event.receive.message.*;
import com.github.cao.awa.bot.network.handler.*;
import com.github.cao.awa.bot.network.packet.*;
import com.github.cao.awa.bot.network.packet.send.message.*;
import com.github.cao.awa.bot.server.*;

public class MessageReceivedPacket extends ReadonlyPacket {
    private final SendMessageType type;
    private final String message;
    private final long botId;
    private final long senderId;
    private final long responseId;

    public MessageReceivedPacket(SendMessageType type, String message, long botId, long senderId, long responseId) {
        this.type = type;
        this.message = message;
        this.botId = botId;
        this.senderId = senderId;
        this.responseId = responseId;
    }

    public long getBotId() {
        return this.botId;
    }

    public long getResponseId() {
        return this.responseId;
    }

    public String getMessage() {
        return this.message;
    }

    public SendMessageType getType() {
        return this.type;
    }

    public long getSenderId() {
        return this.senderId;
    }

    public void fireEvent(ApricotServer server, ApricotProxy proxy) {
        server.fireEvent(new MessageReceivedEvent(
                proxy,
                this
        ));
    }
}
