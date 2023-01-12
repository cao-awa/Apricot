package com.github.cao.awa.apricot.network.packet.receive.response.message;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;

public class SendMessageResponsePacket extends ResponsePacket {
    private final long messageId;

    public SendMessageResponsePacket(long messageId) {
        this.messageId = messageId;
    }

    public static SendMessageResponsePacket create(JSONObject json) {
        return new SendMessageResponsePacket(json.getInteger("message_id"));
    }

    public long getMessageId() {
        return messageId;
    }
}
