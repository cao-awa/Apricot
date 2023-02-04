package com.github.cao.awa.apricot.network.packet.receive.response.message;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;

public class SendMessageResponsePacket extends ResponsePacket {
    private final long messageId;

    public SendMessageResponsePacket(long messageId) {
        this.messageId = messageId;
    }

    public static SendMessageResponsePacket create(JSONObject json) {
        try {
            return new SendMessageResponsePacket(json.getLong("message_id"));
        } catch (Exception e) {
            return new SendMessageResponsePacket(0);
        }
    }

    public long getMessageId() {
        return messageId;
    }
}
