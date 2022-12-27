package com.github.cao.awa.bot.network.packet.factor;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.bot.network.packet.*;

public abstract class PacketFactor {
    public abstract ReadonlyPacket create(JSONObject request);
    public abstract String getName();
}
