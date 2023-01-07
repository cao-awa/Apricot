package com.github.cao.awa.apricot.network.packet.receive.mute.issue;

import com.github.cao.awa.apricot.network.packet.*;

public abstract class IssueGroupMuteReceivedPacket extends ReadonlyPacket {
    public long getDuration() {
        return - 1;
    }

    public long getUserId() {
        return 0;
    }

    public abstract long getGroupId();

    public abstract long getBotId();

    public abstract long getOperatorId();

    public abstract long getTimestamp();
}
