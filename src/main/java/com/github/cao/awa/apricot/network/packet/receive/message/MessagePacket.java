package com.github.cao.awa.apricot.network.packet.receive.message;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;

public abstract class MessagePacket extends ReadonlyPacket {
    public abstract IgnoredIdMessageSender getSender();

    public abstract AssembledMessage getMessage();

    public abstract long getSenderId();

    public abstract long getResponseId();

    public abstract long getTimestamp();

    public abstract int getMessageId();

    public abstract long getBotId();

    public abstract MessageType getType();

    public abstract long getOwnId();

    public abstract long getTargetId();
}
