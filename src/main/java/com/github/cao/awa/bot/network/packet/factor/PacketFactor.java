package com.github.cao.awa.bot.network.packet.factor;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.bot.network.packet.*;
import com.github.cao.awa.bot.server.*;

public abstract class PacketFactor {
    public abstract ReadonlyPacket create(ApricotServer server, JSONObject request);
    public abstract String getName();
}
