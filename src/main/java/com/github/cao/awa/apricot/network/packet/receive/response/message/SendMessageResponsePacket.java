package com.github.cao.awa.apricot.network.packet.receive.response.message;

import com.github.cao.awa.apricot.network.packet.*;

public class SendMessageResponsePacket extends ResponsePacket {
    private final long messageId;

    public SendMessageResponsePacket(long messageId) {
        this.messageId = messageId;
    }

    public long getMessageId() {
        return messageId;
    }
}
