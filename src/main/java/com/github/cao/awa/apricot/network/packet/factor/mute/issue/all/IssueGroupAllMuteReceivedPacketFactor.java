package com.github.cao.awa.apricot.network.packet.factor.mute.issue.all;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.receive.mute.issue.all.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class IssueGroupAllMuteReceivedPacketFactor extends PacketFactor {
    @Override
    public @NotNull IssueGroupAllMuteReceivedPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new IssueGroupAllMuteReceivedPacket(
                request.getLong("self_id"),
                request.getLong("group_id"),
                request.getLong("operator_id"),
                request.getLong("time")
        );
    }

    @Override
    public @NotNull String getName() {
        return "notice-group-ban-all";
    }
}
