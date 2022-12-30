package com.github.cao.awa.apricot.network.packet.factor.message.recall.gruop;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.recall.group.*;
import com.github.cao.awa.apricot.server.*;

public class GroupMessageRecallPacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        return new GroupMessageRecallPacket(
                request.getLong("self_id"),
                request.getLong("user_id"),
                request.getLong("time"),
                request.getLong("operator_id"),
                request.getLong("group_id"),
                request.getLong("message_id")
        );
    }

    @Override
    public String getName() {
        return "notice-group-recall";
    }
}
