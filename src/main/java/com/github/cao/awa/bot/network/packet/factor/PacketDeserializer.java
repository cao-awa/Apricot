package com.github.cao.awa.bot.network.packet.factor;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.bot.network.packet.*;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PacketDeserializer {
    private final Map<String, PacketFactor> factors = new Object2ObjectOpenHashMap<>();

    @Nullable
    public ReadonlyPacket deserializer(JSONObject json) {
        String name = json.getString("post_type");
        if (this.factors.containsKey(name)) {
            return this.factors.get(name).create(json);
        }
        return null;
    }

    public void register(PacketFactor factor) {
        this.factors.put(factor.getName(), factor);
    }
}
