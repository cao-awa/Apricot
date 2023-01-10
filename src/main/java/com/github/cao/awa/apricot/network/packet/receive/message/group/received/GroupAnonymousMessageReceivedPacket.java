package com.github.cao.awa.apricot.network.packet.receive.message.group.received;

import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;

public class GroupAnonymousMessageReceivedPacket extends GroupMessageReceivedPacket {
    private final AssembledMessage message;
    private final long botId;
    private final MessageSender sender;
    private final long responseId;
    private final long timestamp;
    private final int messageId;

    public GroupAnonymousMessageReceivedPacket(AssembledMessage message, long botId, MessageSender sender, long responseId, long timestamp, int messageId) {
        this.message = message;
        this.botId = botId;
        this.sender = sender;
        this.responseId = responseId;
        this.timestamp = timestamp;
        this.messageId = messageId;
    }

    public MessageSender getSender() {
        return this.sender;
    }

    public AssembledMessage getMessage() {
        return this.message;
    }

    public long getSenderId() {
        return this.sender.getUserId();
    }

    public long getResponseId() {
        return this.responseId;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public long getBotId() {
        return this.botId;
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
        server.fireEvent(new GroupAnonymousMessageReceivedEvent(
                proxy,
                this
        ));
    }

    @Override
    public long getGroupId() {
        return this.responseId;
    }
}
