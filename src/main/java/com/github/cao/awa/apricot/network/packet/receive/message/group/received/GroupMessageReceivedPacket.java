package com.github.cao.awa.apricot.network.packet.receive.message.group.received;

import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.util.time.*;

public abstract class GroupMessageReceivedPacket extends MessageReceivedPacket {
    @Override
    public MessageType getType() {
        return MessageType.GROUP;
    }

    @Override
    public EventTarget target() {
        return new EventTarget(
                getGroupId(),
                getSenderId(),
                getBotId()
        );
    }

    public abstract long getGroupId();
}
