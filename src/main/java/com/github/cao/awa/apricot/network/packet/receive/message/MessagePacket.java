package com.github.cao.awa.apricot.network.packet.receive.message;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;

public abstract class MessagePacket extends ReadonlyPacket {
    public abstract MessageSender getSender();

    public abstract AssembledMessage getMessage();

    public abstract long getSenderId();

    public abstract long getResponseId();

    public abstract long getTimestamp();

    public abstract long getMessageId();

    public abstract long getBotId();

    public abstract MessageType getType();

    public abstract long getOwnId();
}