package com.github.cao.awa.apricot.network.packet.factor.message.personal.received;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.receive.message.personal.received.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.message.*;
import org.jetbrains.annotations.*;

public class PrivateTemporaryMessageReceivedPacketFactor extends PacketFactor {
    @Override
    public @NotNull PrivateTemporaryMessageReceivedPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new PrivateTemporaryMessageReceivedPacket(
                MessageUtil.process(
                        server,
                        request.getString("message")
                ),
                request.getLong("self_id"),
                PrivateMessageSender.create(request.getJSONObject("sender")),
                request.getLong("user_id"),
                request.getLong("time"),
                request.getLong("message_seq"),
                request.getLong("message_id")
        );
    }

    @Override
    public @NotNull String getName() {
        return "message-private-group";
    }
}
