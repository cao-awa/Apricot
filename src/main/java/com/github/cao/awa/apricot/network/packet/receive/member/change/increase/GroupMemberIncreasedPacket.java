package com.github.cao.awa.apricot.network.packet.receive.member.change.increase;

import com.github.cao.awa.apricot.approval.group.*;
import com.github.cao.awa.apricot.network.packet.receive.member.change.*;

public abstract class GroupMemberIncreasedPacket extends GroupMemberChangedPacket {
    public abstract ApprovalType getType();
}
