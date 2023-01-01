package com.github.cao.awa.apricot.network.packet.factor.mute.lift.all;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.mute.lift.all.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class LiftGroupAllMuteReceivedPacketFactor extends PacketFactor {
    @Override
    public @NotNull LiftGroupAllMuteReceivedPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new LiftGroupAllMuteReceivedPacket(
                request.getLong("self_id"),
                request.getLong("group_id"),
                request.getLong("operator_id"),
                request.getLong("time")
        );
    }

    @Override
    public @NotNull String getName() {
        return "notice-group-lift-all";
    }
}
