package com.github.cao.awa.apricot.network.packet.factor.mute.issue.personal;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.receive.mute.issue.personal.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class IssueGroupPersonalMuteReceivedPacketFactor extends PacketFactor {
    @Override
    public @NotNull IssueGroupPersonalMuteReceivedPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new IssueGroupPersonalMuteReceivedPacket(
                request.getLong("self_id"),
                request.getLong("user_id"),
                request.getLong("group_id"),
                request.getLong("operator_id"),
                request.getLong("time"),
                request.getLong("duration")
        );
    }

    @Override
    public @NotNull String getName() {
        return "notice-group-ban-personal";
    }
}
