package com.github.cao.awa.apricot.network.packet.receive.message;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;
import com.github.cao.awa.apricot.util.time.TimeUtil;

public abstract class MessagePacket extends ReadonlyPacket {
    private final long ownId = TimeUtil.nanoId();

    public abstract IgnoredIdMessageSender getSender();

    public abstract AssembledMessage getMessage();

    public abstract long getSenderId();

    public abstract long getResponseId();

    public abstract long getTimestamp();

    public abstract long getMessageSeq();

    public abstract long getBotId();

    public abstract MessageType getType();

    @Deprecated
    public long getOwnId() {
        return this.ownId;
    }

    public abstract long getTargetId();
}
