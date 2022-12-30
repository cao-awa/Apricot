package com.github.cao.awa.apricot.network.packet.factor.member.change.decrease.kick;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.member.change.decrease.leave.*;
import com.github.cao.awa.apricot.network.packet.recevied.member.change.increase.approve.*;
import com.github.cao.awa.apricot.server.*;

public class GroupMemberLeavedPacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        return new GroupMemberLeavedPacket(
                request.getLong("operator_id"),
                request.getLong("self_id"),
                request.getLong("user_id"),
                request.getLong("group_id"),
                request.getLong("time")
        );
    }

    @Override
    public String getName() {
        return "notice-group-decrease-leave";
    }
}
