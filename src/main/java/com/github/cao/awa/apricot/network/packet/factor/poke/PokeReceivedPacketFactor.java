package com.github.cao.awa.apricot.network.packet.factor.poke;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.sender.*;
import com.github.cao.awa.apricot.network.packet.recevied.poke.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.message.*;

public class PokeReceivedPacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        SendMessageType messageType = request.containsKey("group_id") ? SendMessageType.GROUP : SendMessageType.PRIVATE;
        long sender = request.getLong("sender_id");
        long responseId = messageType == SendMessageType.GROUP ? request.getLong("group_id") : sender;
        return new PokeReceivedPacket(
                messageType,
                sender,
                request.getLong("target_id"),
                request.getLong("self_id"),
                responseId,
                request.getLong("time")
        );
    }

    @Override
    public String getName() {
        return "notice-poke";
    }
}
