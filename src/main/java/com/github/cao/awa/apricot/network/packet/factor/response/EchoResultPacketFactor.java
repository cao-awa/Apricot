package com.github.cao.awa.apricot.network.packet.factor.response;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class EchoResultPacketFactor extends PacketFactor {
    @Override
    public @NotNull EchoResultPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new EchoResultPacket(
                request.getString("id"),
                request
        );
    }

    @Override
    public @NotNull String getName() {
        return "echo-result";
    }
}
