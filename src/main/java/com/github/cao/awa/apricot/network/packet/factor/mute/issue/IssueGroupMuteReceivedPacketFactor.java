package com.github.cao.awa.apricot.network.packet.factor.mute.issue;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.mute.issue.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class IssueGroupMuteReceivedPacketFactor extends PacketFactor {
    @Override
    public @NotNull ReadonlyPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return request.getLong("user_id") == 0 ?
               server.createPacket(
                       "notice-group-ban-all",
                       request
               ) :
               server.createPacket(
                       "notice-group-ban-personal",
                       request
               );
    }

    @Override
    public @NotNull String getName() {
        return "notice-group-ban-ban";
    }
}
