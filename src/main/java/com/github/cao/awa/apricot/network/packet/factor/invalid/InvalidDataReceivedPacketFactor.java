package com.github.cao.awa.apricot.network.packet.factor.invalid;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.invalid.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class InvalidDataReceivedPacketFactor extends PacketFactor {
    @Override
    public @NotNull InvalidDataReceivedPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new InvalidDataReceivedPacket(
                request,
                false
        );
    }

    @Override
    public @NotNull String getName() {
        return "invalid-data";
    }
}
