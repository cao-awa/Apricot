package com.github.cao.awa.apricot.network.packet.recevied.member.change;

import com.github.cao.awa.apricot.network.packet.*;

public abstract class GroupMemberChangedPacket extends ReadonlyPacket {
    public abstract long getOperatorId();

    public abstract long getBotId();

    public abstract long getUserId();

    public abstract long getGroupId();

    public abstract long getTimestamp();
}
