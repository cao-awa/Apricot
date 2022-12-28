package com.github.cao.awa.apricot.network.packet.factor.response;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.response.*;
import com.github.cao.awa.apricot.server.*;

public class EchoResultPacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        return new EchoResultPacket(request.getString("id"));
    }

    @Override
    public String getName() {
        return "echo-result";
    }
}
