package com.github.cao.awa.apricot.store;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.message.*;

public class MessageStore {
    private final Message<?, ?> message;
    private final long senderId;
    private final long groupId;

    public MessageStore(Message<?, ?> message, long senderId, long groupId) {
        this.message = message;
        this.senderId = senderId;
        this.groupId = groupId;
    }

    public static MessageStore fromPacket(MessageReceivedPacket packet) {
        return new MessageStore(
                packet.getMessage(),
                packet.getSenderId(),
                packet.getType() == MessageType.GROUP ? packet.getResponseId() : - 1
        );
    }

    public static MessageStore fromJSONObject(ApricotServer server, JSONObject json) {
        MessageStore store = new MessageStore(
                MessageUtil.process(
                        server,
                        json.getString("m")
                ),
                json.getLong("s"),
                json.containsKey("g") ? json.getLong("g") : - 1
        );
        return store;
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
        if (this.groupId != - 1) {
            json.fluentPut(
                    "g",
                    this.groupId
            );
        }
        return json;
    }
}
