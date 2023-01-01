package com.github.cao.awa.apricot.network.packet.factor.meta.lifecycle;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.meta.lifecycle.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class ProxyDisconnectPacketFactor extends PacketFactor {
    @Override
    public @NotNull ProxyDisconnectPacket create(@NotNull ApricotServer server, @NotNull JSONObject request) {
        return new ProxyDisconnectPacket(
                request.getString("disconnect_reason"),
                request.getLong("proxy_id"),
                request.getLong("connect_time")
        );
    }

    @Override
    public @NotNull String getName() {
        return "meta-lifecycle-disconnect";
    }
}
