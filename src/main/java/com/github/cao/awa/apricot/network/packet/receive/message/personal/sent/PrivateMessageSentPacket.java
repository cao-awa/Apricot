package com.github.cao.awa.apricot.network.packet.receive.message.personal.sent;

import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.util.time.*;

public abstract class PrivateMessageSentPacket extends MessageSentPacket {
    @Override
    public MessageType getType() {
        return MessageType.PRIVATE;
    }

    @Override
    public EventTarget target() {
        return new EventTarget(
                - 1,
                getSenderId(),
                getBotId()
        );
    }
}
