package com.github.cao.awa.apricot.network.packet.recevied.message.recall;

import com.github.cao.awa.apricot.network.packet.*;

public abstract class MessageRecallPacket extends ReadonlyPacket {
    public abstract long getMessageId();

    public abstract long getTimestamp();

    public abstract long getBotId();

    public abstract long getResponseId();
}
