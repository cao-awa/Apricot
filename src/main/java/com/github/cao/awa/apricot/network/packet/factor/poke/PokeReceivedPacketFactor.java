package com.github.cao.awa.apricot.network.packet.factor.poke;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.poke.*;
import com.github.cao.awa.apricot.server.*;

public class PokeReceivedPacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        MessageType messageType = request.containsKey("group_id") ? MessageType.GROUP : MessageType.PRIVATE;
        long sender = request.getLong("sender_id");
        long responseId = messageType == MessageType.GROUP ? request.getLong("group_id") : sender;
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
        return "notice-notify-poke";
    }
}