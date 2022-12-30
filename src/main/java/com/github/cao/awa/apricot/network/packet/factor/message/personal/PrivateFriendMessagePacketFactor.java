package com.github.cao.awa.apricot.network.packet.factor.message.personal;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.personal.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.sender.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.message.*;

public class PrivateFriendMessagePacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        return new PrivateFriendMessageReceivedPacket(
                MessageUtil.process(
                        server,
                        request.getString("message")
                ),
                request.getLong("self_id"),
                PrivateMessageSender.create(request.getJSONObject("sender")),
                request.getLong("user_id"),
                request.getLong("time"),
                request.getLong("message_id")
        );
    }

    @Override
    public String getName() {
        return "message-private-friend";
    }
}
