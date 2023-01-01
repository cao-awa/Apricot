package com.github.cao.awa.apricot.network.packet.factor.add.friend;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.add.friend.*;
import com.github.cao.awa.apricot.network.packet.recevied.add.group.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class AddFriendReceivedPacketFactor extends PacketFactor {
    @Override
    public @NotNull AddFriendReceivedPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new AddFriendReceivedPacket(
                request.getString("comment"),
                request.getLong("self_id"),
                request.getLong("user_id"),
                request.getLong("time"),
                request.getString("flag")
        );
    }

    @Override
    public @NotNull String getName() {
        return "request-friend";
    }
}
