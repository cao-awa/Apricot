package com.github.cao.awa.apricot.network.packet.factor.member.change.decrease.leave;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.member.change.decrease.kick.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class GroupMemberKickedPacketFactor extends PacketFactor {
    @Override
    public @NotNull GroupMemberKickedPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new GroupMemberKickedPacket(
                request.getLong("operator_id"),
                request.getLong("self_id"),
                request.getLong("user_id"),
                request.getLong("group_id"),
                request.getLong("time")
        );
    }

    @Override
    public @NotNull String getName() {
        return "notice-group-decrease-kick";
    }
}
