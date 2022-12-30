package com.github.cao.awa.apricot.network.packet.recevied.message.personal;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;

public abstract class PrivateMessageReceivedPacket extends MessageReceivedPacket {
    @Override
    public MessageType getType() {
        return MessageType.PRIVATE;
    }
}
