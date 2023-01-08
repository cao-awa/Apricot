package com.github.cao.awa.apricot.message.store;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.message.*;

public class MessageStore {
    private AssembledMessage message;
    private long senderId;
    private long targetId;
    private long messageId;
    private boolean recalled;

    public MessageStore(AssembledMessage message, long senderId, long targetId, long messageId, boolean recalled) {
        this.message = message;
        this.senderId = senderId;
        this.targetId = targetId;
        this.messageId = messageId;
        this.recalled = recalled;
    }

    public static MessageStore fromPacket(MessagePacket packet) {
        return new MessageStore(
                packet.getMessage(),
                packet.getSenderId(),
                packet.getResponseId(),
                packet.getMessageId(),
                false
        );
    }

    public static MessageStore fromJSONObject(ApricotServer server, JSONObject json) {
        return new MessageStore(
                MessageUtil.process(
                        server,
                        json.getString("m")
                ),
                json.getLong("s"),
                json.getLong("a"),
                json.getLong("r"),
                json.containsKey("c")
        );
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

    public long getTargetId() {
        return this.targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
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
        json.fluentPut(
                "a",
                this.targetId
        );
        if (this.recalled) {
            json.fluentPut(
                    "c",
                    0
            );
        }
        return json;
    }

    public boolean isRecalled() {
        return this.recalled;
    }

    public void setRecalled(boolean recalled) {
        this.recalled = recalled;
    }
}
