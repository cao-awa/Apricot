package com.github.cao.awa.apricot.network.packet.receive.poke;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.*;

public abstract class PokeReceivedPacket extends ReadonlyPacket {
    public abstract MessageType getType();

    public abstract long getCauserId();

    public abstract long getBotId();

    public abstract long getTargetId();

    public abstract long getResponseId();

    public abstract long getTimestamp();
}
