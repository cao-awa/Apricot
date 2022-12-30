package com.github.cao.awa.apricot.network.packet.factor.message.group;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.group.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.sender.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.sender.anonymous.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.message.*;

public class GroupAnonymousMessagePacketFactor extends GroupMessageReceivedFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        return new GroupAnonymousMessageReceivedPacket(
                MessageUtil.process(
                        server,
                        request.getString("message")
                ),
                request.getLong("self_id"),
                AnonymousMessageSender.create(request.getJSONObject("anonymous"), request.getJSONObject("sender")),
                request.getLong("group_id"),
                request.getLong("time"),
                request.getString("message_id")
        );
    }

    @Override
    public String getName() {
        return "message-group-anonymous";
    }
}
