package com.github.cao.awa.apricot.network.packet.factor.add.group;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.add.group.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class AddGroupReceivedPacketFactor extends PacketFactor {
    @Override
    public @NotNull AddGroupReceivedPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new AddGroupReceivedPacket(
                request.getString("comment"),
                request.getLong("self_id"),
                request.getLong("user_id"),
                request.getLong("group_id"),
                request.getLong("time"),
                request.getString("flag")
        );
    }

    @Override
    public @NotNull String getName() {
        return "request-group-add";
    }
}
