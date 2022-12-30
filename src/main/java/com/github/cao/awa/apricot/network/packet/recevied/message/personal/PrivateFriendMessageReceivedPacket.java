package com.github.cao.awa.apricot.network.packet.recevied.message.personal;

import com.github.cao.awa.apricot.event.receive.accomplish.message.personal.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.sender.*;
import com.github.cao.awa.apricot.server.*;

public class PrivateFriendMessageReceivedPacket extends PrivateMessageReceivedPacket {
    private final AssembledMessage message;
    private final long botId;
    private final MessageSender sender;
    private final long responseId;
    private final long timestamp;
    private final String messageId;

    public PrivateFriendMessageReceivedPacket(AssembledMessage message, long botId, MessageSender sender, long responseId, long timestamp, String messageId) {
        this.message = message;
        this.botId = botId;
        this.sender = sender;
        this.responseId = responseId;
        this.timestamp = timestamp;
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
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

    public AssembledMessage getMessage() {
        return this.message;
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
        server.fireEvent(new PrivateFriendMessageReceivedEvent(
                proxy,
                this
        ));
    }
}
