package com.github.cao.awa.apricot.store;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.message.*;

public class MessageStore {
    private AssembledMessage message;
    private long senderId;
    private long groupId;
    private long messageId;
    private boolean recalled;

    public MessageStore(AssembledMessage message, long senderId, long groupId, long messageId, boolean recalled) {
        this.message = message;
        this.senderId = senderId;
        this.groupId = groupId;
        this.messageId = messageId;
        this.recalled = recalled;
    }

    public static MessageStore fromPacket(MessagePacket packet) {
        return new MessageStore(
                packet.getMessage(),
                packet.getSenderId(),
                packet.getType() == MessageType.GROUP ? packet.getResponseId() : - 1,
                packet.getMessageId(),
                false
        );
    }

    public static MessageStore fromJSONObject(ApricotServer server, JSONObject json) {
        MessageStore store = new MessageStore(
                MessageUtil.process(
                        server,
                        json.getString("m")
                ),
                json.getLong("s"),
                json.containsKey("g") ? json.getLong("g") : - 1,
                json.getLong("r"),
                json.containsKey("c")
        );
        return store;
    }

    public AssembledMessage getMessage() {
        return this.message;
    }

    public void setMessage(AssembledMessage message) {
        this.message = message;
    }

    public long getSenderId() {
        return this.senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getMessageId() {
        return this.messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.fluentPut(
                "m",
                this.message.toPlainText()
        );
        json.fluentPut(
                "s",
                this.senderId
        );
        json.fluentPut(
                "r",
                this.messageId
        );
        if (this.groupId != - 1) {
            json.fluentPut(
                    "g",
                    this.groupId
            );
        }
        if (this.recalled) {
            json.fluentPut(
                    "c",
                    0
            );
        }
        return json;
    }

    public boolean isRecalled() {
        return recalled;
    }

    public void setRecalled(boolean recalled) {
        this.recalled = recalled;
    }
}
