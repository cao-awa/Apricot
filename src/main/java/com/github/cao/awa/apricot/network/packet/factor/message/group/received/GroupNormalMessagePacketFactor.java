package com.github.cao.awa.apricot.network.packet.factor.message.group.received;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.receive.message.group.received.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.message.*;
import org.jetbrains.annotations.*;

public class GroupNormalMessagePacketFactor extends PacketFactor {
    @Override
    public @NotNull GroupNormalMessageReceivedPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new GroupNormalMessageReceivedPacket(
                MessageUtil.process(
                        server,
                        request.getString("message")
                ),
                request.getLong("self_id"),
                GroupMessageSender.create(request.getJSONObject("sender")),
                request.getLong("group_id"),
                request.getLong("time"),
                request.getLong("message_seq"),
                request.getLong("message_id")
        );
    }

    @Override
    public @NotNull String getName() {
        return "message-group-normal";
    }
}
