package com.github.cao.awa.apricot.network.packet.recevied.message;

import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.sender.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.server.*;

public class MessageReceivedPacket extends ReadonlyPacket {
    private final SendMessageType type;
    private final Message message;
    private final long botId;
    private final MessageSender sender;
    private final long responseId;
    private final long timestamp;
    private final long messageId;
    private final long targetId;

    public MessageReceivedPacket(SendMessageType type, Message message, long botId, MessageSender sender, long responseId, long timestamp, long messageId, long targetId) {
        this.type = type;
        this.message = message;
        this.botId = botId;
        this.sender = sender;
        this.responseId = responseId;
        this.timestamp = timestamp;
        this.messageId = messageId;
        this.targetId = targetId;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getTargetId() {
        return targetId;
    }

    public long getTimestamp() {
        return timestamp;
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

    /**
     * Let an event of this packet be fired.
     *
     * @param server
     *         apricot server
     * @param proxy
     *         apricot proxy
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void fireEvent(ApricotServer server, ApricotProxy proxy) {
        server.fireEvent(new MessageReceivedEvent(
                proxy,
                this
        ));
    }
}
