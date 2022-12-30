package com.github.cao.awa.apricot.network.packet.factor.meta.lifecycle;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.meta.lifecycle.*;
import com.github.cao.awa.apricot.server.*;

public class ProxyDisconnectPacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        return new ProxyDisconnectPacket(
                request.getString("disconnect_reason"),
                request.getLong("proxy_id"),
                request.getLong("connect_time")
        );
    }

    @Override
    public String getName() {
        return "meta-lifecycle-disconnect";
    }
}
