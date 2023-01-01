package com.github.cao.awa.apricot.network.packet.factor.meta.lifecycle;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.meta.lifecycle.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class ProxyConnectPacketFactor extends PacketFactor {
    @Override
    public @NotNull ProxyConnectPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new ProxyConnectPacket(
                request.getLong("self_id"),
                request.getLong("time")
        );
    }

    @Override
    public @NotNull String getName() {
        return "meta-lifecycle-connect";
    }
}
