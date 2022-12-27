package com.github.cao.awa.bot.network.packet.factor.message;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.bot.event.receive.message.*;
import com.github.cao.awa.bot.network.packet.*;
import com.github.cao.awa.bot.network.packet.factor.*;
import com.github.cao.awa.bot.network.packet.recevied.message.*;
import com.github.cao.awa.bot.network.packet.recevied.message.sender.*;
import com.github.cao.awa.bot.network.packet.send.message.*;
import com.github.cao.awa.bot.server.*;
import com.github.cao.awa.bot.utils.message.*;

public class MessageReceivedPacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        SendMessageType messageType = SendMessageType.of(request.getString("message_type"));
        long userId = request.getLong("user_id");
        return new MessageReceivedPacket(
                messageType,
                MessageUtil.process(server, request.getString("message")),
                request.getLong("self_id"),
                messageType == SendMessageType.PRIVATE ?
                PribvateMessageSender.create(request.getJSONObject("sender")) :
                GroupMessageSender.create(request.getJSONObject("sender")),
                messageType == SendMessageType.PRIVATE ?
                userId :
                messageType == SendMessageType.GROUP ? request.getLong("group_id") : - 1,
                request.getLong("time")
        );
    }

    @Override
    public String getName() {
        return "message";
    }
}
