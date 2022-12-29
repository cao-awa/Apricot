package com.github.cao.awa.apricot.network.packet.factor.invalid;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.factor.*;
import com.github.cao.awa.apricot.network.packet.recevied.invalid.*;
import com.github.cao.awa.apricot.server.*;

public class InvalidDataReceivedPacketFactor extends PacketFactor {
    @Override
    public ReadonlyPacket create(ApricotServer server, JSONObject request) {
        return new InvalidDataReceivedPacket(
                request,
                false
        );
    }

    @Override
    public String getName() {
        return "invalid-data";
    }
}
