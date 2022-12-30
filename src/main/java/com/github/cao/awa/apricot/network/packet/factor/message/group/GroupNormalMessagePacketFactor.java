package com.github.cao.awa.apricot.network.packet.factor.message.group;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.group.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.sender.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.message.*;

public class GroupNormalMessagePacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        return new GroupNormalMessageReceivedPacket(
                MessageUtil.process(
                        server,
                        request.getString("message")
                ),
                request.getLong("self_id"),
                GroupMessageSender.create(request.getJSONObject("sender")),
                request.getLong("group_id"),
                request.getLong("time"),
                request.getLong("message_id")
        );
    }

    @Override
    public String getName() {
        return "message-group-normal";
    }
}
