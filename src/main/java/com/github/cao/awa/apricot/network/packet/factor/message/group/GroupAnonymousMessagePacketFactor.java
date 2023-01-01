package com.github.cao.awa.apricot.network.packet.factor.message.group;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.group.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.sender.anonymous.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.message.*;
import org.jetbrains.annotations.*;

public class GroupAnonymousMessagePacketFactor extends PacketFactor {
    @Override
    public @NotNull GroupAnonymousMessageReceivedPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new GroupAnonymousMessageReceivedPacket(
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
        return "message-group-anonymous";
    }
}
