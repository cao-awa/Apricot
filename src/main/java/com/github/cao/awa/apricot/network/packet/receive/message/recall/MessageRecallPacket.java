package com.github.cao.awa.apricot.network.packet.receive.message.recall;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.*;

public abstract class MessageRecallPacket extends ReadonlyPacket {
    public abstract MessageType getType();

    public abstract int getMessageId();

    public abstract long getTimestamp();

    public abstract long getBotId();

    public abstract long getResponseId();
}
