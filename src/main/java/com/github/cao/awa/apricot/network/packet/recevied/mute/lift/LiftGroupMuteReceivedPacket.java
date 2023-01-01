package com.github.cao.awa.apricot.network.packet.recevied.mute.lift;

import com.github.cao.awa.apricot.network.packet.*;

public abstract class LiftGroupMuteReceivedPacket extends ReadonlyPacket {
    public long getUserId() {
        return 0;
    }

    public abstract long getGroupId();

    public abstract long getBotId();

    public abstract long getOperatorId();

    public abstract long getTimestamp();
}
