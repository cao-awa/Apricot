package com.github.cao.awa.apricot.network.packet.factor.message.recall.personal;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.recall.personal.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class PrivateMessageRecallPacketFactor extends PacketFactor {
    @Override
    public @NotNull PrivateMessageRecallPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new PrivateMessageRecallPacket(
                request.getLong("self_id"),
                request.getLong("user_id"),
                request.getLong("time"),
                request.getLong("message_id")
        );
    }

    @Override
    public @NotNull String getName() {
        return "notice-friend-recall";
    }
}
