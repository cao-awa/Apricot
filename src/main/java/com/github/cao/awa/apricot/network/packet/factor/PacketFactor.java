package com.github.cao.awa.apricot.network.packet.factor;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public abstract class PacketFactor {
    @NotNull
    public abstract ReadonlyPacket create(@NotNull ApricotServer server, @NotNull JSONObject request);

    @NotNull
    public abstract String getName();
}
