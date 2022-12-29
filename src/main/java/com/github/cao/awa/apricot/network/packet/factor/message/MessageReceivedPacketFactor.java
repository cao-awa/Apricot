package com.github.cao.awa.apricot.network.packet.factor.message;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.sender.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.message.*;

public class MessageReceivedPacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        SendMessageType messageType = SendMessageType.of(request.getString("message_type"));
        long userId = request.getLong("user_id");
        long targetId = request.containsKey("target_id") ? request.getLong("target_id") : - 1;
        return new MessageReceivedPacket(
                messageType,
                MessageUtil.process(
                        server,
                        request.getString("message")
                ),
                request.getLong("self_id"),
                messageType == SendMessageType.PRIVATE ?
                PribvateMessageSender.create(request.getJSONObject("sender")) :
                messageType == SendMessageType.GROUP ?
                GroupMessageSender.create(request.getJSONObject("sender")) :
                new GuildMessageSender(),
                messageType == SendMessageType.PRIVATE ?
                userId :
                messageType == SendMessageType.GROUP ? request.getLong("group_id") : - 1,
                request.getLong("time"),
                request.getString("message_id"),
                targetId
        );
    }

    @Override
    public String getName() {
        return "message-normal";
    }
}
