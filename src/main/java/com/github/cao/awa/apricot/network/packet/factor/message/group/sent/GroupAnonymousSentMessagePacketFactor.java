package com.github.cao.awa.apricot.network.packet.factor.message.group.sent;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.receive.message.group.sent.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.anonymous.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.message.*;
import org.jetbrains.annotations.*;

public class GroupAnonymousSentMessagePacketFactor extends PacketFactor {
    @Override
    public @NotNull GroupAnonymousMessageSentPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new GroupAnonymousMessageSentPacket(
                MessageUtil.process(
                        server,
                        request.getString("message")
                ),
                request.getLong("self_id"),
                AnonymousMessageSender.create(request.getJSONObject("anonymous"), request.getJSONObject("sender")),
                request.getLong("group_id"),
                request.getLong("time"),
                request.getLong("message_id")
        );
    }

    @Override
    public @NotNull String getName() {
        return "message-sent-group-anonymous";
    }
}
