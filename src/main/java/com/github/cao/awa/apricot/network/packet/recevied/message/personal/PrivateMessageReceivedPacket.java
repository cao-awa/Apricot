package com.github.cao.awa.apricot.network.packet.recevied.message.personal;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;
import com.github.cao.awa.apricot.utils.time.*;

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
}
