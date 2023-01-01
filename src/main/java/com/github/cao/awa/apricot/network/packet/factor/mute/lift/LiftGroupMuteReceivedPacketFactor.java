package com.github.cao.awa.apricot.network.packet.factor.mute.lift;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class LiftGroupMuteReceivedPacketFactor extends PacketFactor {
    @Override
    public @NotNull ReadonlyPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return request.getLong("user_id") == 0 ?
               server.createPacket(
                       "notice-group-lift-all",
                       request
               ) :
               server.createPacket(
                       "notice-group-lift-personal",
                       request
               );
    }

    @Override
    public @NotNull String getName() {
        return "notice-group-ban-lift-ban";
    }
}
