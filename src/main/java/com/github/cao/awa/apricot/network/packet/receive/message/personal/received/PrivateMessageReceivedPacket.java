package com.github.cao.awa.apricot.network.packet.receive.message.personal.received;

import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.util.time.*;

public abstract class PrivateMessageReceivedPacket extends MessageReceivedPacket {
    private final long ownId = TimeUtil.nanoId();

    @Override
    public MessageType getType() {
        return MessageType.PRIVATE;
    }

    @Override
    public long getOwnId() {
        return ownId;
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
