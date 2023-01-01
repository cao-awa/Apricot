package com.github.cao.awa.apricot.network.packet.factor.mute.lift.personal;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.mute.lift.personal.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class LiftGroupPersonalMuteReceivedPacketFactor extends PacketFactor {
    @Override
    public @NotNull LiftGroupPersonalMuteReceivedPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new LiftGroupPersonalMuteReceivedPacket(
                request.getLong("self_id"),
                request.getLong("user_id"),
                request.getLong("group_id"),
                request.getLong("operator_id"),
                request.getLong("time")
        );
    }

    @Override
    public @NotNull String getName() {
        return "notice-group-lift-personal";
    }
}
