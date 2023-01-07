package com.github.cao.awa.apricot.network.packet.receive.message.group.received;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.target.*;
import com.github.cao.awa.apricot.utils.time.*;

public abstract class GroupMessageReceivedPacket extends MessageReceivedPacket {
    private final long ownId = TimeUtil.nanoId();

    @Override
    public MessageType getType() {
        return MessageType.GROUP;
    }

    @Override
    public long getOwnId() {
        return this.ownId;
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
