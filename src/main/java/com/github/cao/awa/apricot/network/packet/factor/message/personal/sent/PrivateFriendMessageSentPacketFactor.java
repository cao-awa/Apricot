package com.github.cao.awa.apricot.network.packet.factor.message.personal.sent;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.receive.message.personal.sent.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.message.*;
import org.jetbrains.annotations.*;

public class PrivateFriendMessageSentPacketFactor extends PacketFactor {
    @Override
    public @NotNull PrivateFriendMessageSentPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new PrivateFriendMessageSentPacket(
                MessageUtil.process(
                        server,
                        request.getString("message")
                ),
                request.getLong("self_id"),
                PrivateMessageSender.create(request.getJSONObject("sender")),
                request.getLong("target_id"),
                request.getLong("target_id"),
                request.getLong("time"),
                request.getInteger("message_seq")
        );
    }

    @Override
    public @NotNull String getName() {
        return "message-sent-private-friend";
    }
}
