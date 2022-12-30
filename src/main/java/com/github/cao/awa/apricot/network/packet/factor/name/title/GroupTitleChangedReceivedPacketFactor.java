package com.github.cao.awa.apricot.network.packet.factor.name.title;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.name.title.*;
import com.github.cao.awa.apricot.server.*;

public class GroupTitleChangedReceivedPacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        return new GroupTitleChangedReceivedPacket(
                request.getString("title"),
                request.getLong("self_id"),
                request.getLong("user_id"),
                request.getLong("group_id"),
                request.getLong("time")
        );
    }

    @Override
    public String getName() {
        return "notice-notify-title";
    }
}
