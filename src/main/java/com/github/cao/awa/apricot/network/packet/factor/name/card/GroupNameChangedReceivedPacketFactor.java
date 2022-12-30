package com.github.cao.awa.apricot.network.packet.factor.name.card;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.name.card.*;
import com.github.cao.awa.apricot.server.*;

public class GroupNameChangedReceivedPacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        return new GroupNameChangedReceivedPacket(
                request.getString("card_old"),
                request.getString("card_new"),
                request.getLong("self_id"),
                request.getLong("user_id"),
                request.getLong("group_id"),
                request.getLong("time")
        );
    }

    @Override
    public String getName() {
        return "notice-group-card";
    }
}
