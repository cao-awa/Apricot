package com.github.cao.awa.apricot.network.packet.factor;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.server.*;

public abstract class PacketFactor {
    public abstract ReadonlyPacket create(ApricotServer server, JSONObject request);
    public abstract String getName();
}
