package com.github.cao.awa.apricot.network.packet.receive.response.message.get;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.message.*;

public class GetMessageResponsePacket extends ResponsePacket {
    private final BasicMessageSender sender;
    private final MessageType type;
    private final int messageId;
    private final long ownId;
    private final long targetId;
    private final AssembledMessage message;
    private final long timestamp;

    public GetMessageResponsePacket(BasicMessageSender sender, AssembledMessage message, MessageType type, long targetId, int messageId, long ownId, long timestamp) {
        this.sender = sender;
        this.type = type;
        this.targetId = targetId;
        this.messageId = messageId;
        this.ownId = ownId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static GetMessageResponsePacket create(ApricotServer server, JSONObject json) {
        int messageId = json.getInteger("message_id");
        BasicMessageSender sender = new BasicMessageSender(
                json.getLong("user_id"),
                json.getString("nickname")
        );
        return new GetMessageResponsePacket(
                sender,
                MessageUtil.process(
                        server,
                        json.getString("raw_message")
                ),
                MessageType.of(json.getString("message_type")),
                json.containsKey("group_id") ? json.getLong("group_id") : - 1,
                messageId,
                server.getMessagesHeadOffice()
                      .getConvert(messageId),
                json.getLong("time")
        );
    }

    public long getTargetId() {
        return this.targetId;
    }

    public MessageType getType() {
        return this.type;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public long getOwnId() {
        return this.ownId;
    }

    public AssembledMessage getMessage() {
        return this.message;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public BasicMessageSender getSender() {
        return this.sender;
    }
}
