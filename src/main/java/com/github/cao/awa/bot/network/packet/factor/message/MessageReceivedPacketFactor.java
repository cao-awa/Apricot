package com.github.cao.awa.bot.network.packet.factor.message;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.bot.event.receive.message.*;
import com.github.cao.awa.bot.network.packet.*;
import com.github.cao.awa.bot.network.packet.factor.*;
import com.github.cao.awa.bot.network.packet.recevied.message.*;
import com.github.cao.awa.bot.network.packet.send.message.*;

public class MessageReceivedPacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(JSONObject request) {
        SendMessageType messageType = SendMessageType.of(request.getString("message_type"));
        long userId = request.getLong("user_id");
        return new MessageReceivedPacket(
                messageType,
                request.getString("message"),
                request.getLong("self_id"),
                userId,
                messageType == SendMessageType.PRIVATE ? userId : messageType == SendMessageType.GROUP ? request.getLong("group_id") : -1
        );
    }

    @Override
    public String getName() {
        return "message";
    }
}
