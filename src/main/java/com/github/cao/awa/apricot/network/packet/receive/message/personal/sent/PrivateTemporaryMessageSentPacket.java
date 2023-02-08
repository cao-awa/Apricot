package com.github.cao.awa.apricot.network.packet.receive.message.personal.sent;

import com.github.cao.awa.apricot.event.receive.message.personal.sent.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;

public class PrivateTemporaryMessageSentPacket extends PrivateMessageSentPacket {
    private final AssembledMessage message;
    private final long botId;
    private final IgnoredIdMessageSender sender;
    private final long targetId;
    private final long responseId;
    private final long timestamp;
    private final long messageId;

    public PrivateTemporaryMessageSentPacket(AssembledMessage message, long botId, IgnoredIdMessageSender sender, long targetId, long responseId, long timestamp, long messageId) {
        this.message = message;
        this.botId = botId;
        this.targetId = targetId;
        this.sender = sender;
        this.responseId = responseId;
        this.timestamp = timestamp;
        this.messageId = messageId;
    }

    public IgnoredIdMessageSender getSender() {
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

    public long getMessageId() {
        return this.messageId;
    }

    public long getBotId() {
        return this.botId;
    }

    @Override
    public long getTargetId() {
        return this.targetId;
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
        server.fireEvent(new PrivateTemporaryMessageSentEvent(
                proxy,
                this
        ));
    }
}
