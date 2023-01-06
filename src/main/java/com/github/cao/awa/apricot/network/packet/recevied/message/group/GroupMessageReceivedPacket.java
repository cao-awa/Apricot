package com.github.cao.awa.apricot.network.packet.recevied.message.group;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;
import com.github.cao.awa.apricot.utils.time.*;

public abstract class GroupMessageReceivedPacket extends MessageReceivedPacket {
    private final long ownId = TimeUtil.nanoId();

    public abstract long getGroupId();

    @Override
    public MessageType getType() {
        return MessageType.GROUP;
    }

    @Override
    public long getOwnId() {
        return this.ownId;
    }
}
