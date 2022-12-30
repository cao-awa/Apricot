package com.github.cao.awa.apricot.network.packet.recevied.message.group;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;

public abstract class GroupMessageReceivedPacket extends MessageReceivedPacket {
    public abstract long getGroupId();

    @Override
    public MessageType getType() {
        return MessageType.GROUP;
    }
}
