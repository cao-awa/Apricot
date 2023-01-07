package com.github.cao.awa.apricot.network.packet.factor.name.title;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.receive.name.title.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class GroupTitleChangedReceivedPacketFactor extends PacketFactor {
    @Override
    public @NotNull GroupTitleChangedReceivedPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new GroupTitleChangedReceivedPacket(
                request.getString("title"),
                request.getLong("self_id"),
                request.getLong("user_id"),
                request.getLong("group_id"),
                request.getLong("time")
        );
    }

    @Override
    public @NotNull String getName() {
        return "notice-notify-title";
    }
}
