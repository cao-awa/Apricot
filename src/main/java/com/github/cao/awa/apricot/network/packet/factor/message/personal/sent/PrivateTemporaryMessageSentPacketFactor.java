package com.github.cao.awa.apricot.network.packet.factor.message.personal.sent;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.receive.message.personal.sent.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.message.*;
import org.jetbrains.annotations.*;

public class PrivateTemporaryMessageSentPacketFactor extends PacketFactor {
    @Override
    public @NotNull PrivateTemporaryMessageSentPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new PrivateTemporaryMessageSentPacket(
                MessageUtil.process(
                        server,
                        request.getString("message")
                ),
                request.getLong("self_id"),
                PrivateMessageSender.create(request.getJSONObject("sender")),
                request.getLong("target_id"),
                request.getLong("target_id"),
                request.getLong("time"),
                request.getInteger("message_id")
        );
    }

    @Override
    public @NotNull String getName() {
        return "message-sent-private-group";
    }
}
