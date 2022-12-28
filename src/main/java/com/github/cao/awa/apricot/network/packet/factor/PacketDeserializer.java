package com.github.cao.awa.apricot.network.packet.factor;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.server.*;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PacketDeserializer {
    private final Map<String, PacketFactor> factors = new Object2ObjectOpenHashMap<>();

    @Nullable
    public ReadonlyPacket deserializer(ApricotServer server, JSONObject request) {
        String name;
        if (request.containsKey("post_type")) {
            name = request.getString("post_type");
        } else {
            request = request.getJSONObject("echo");
            name = request.getString("type");
        }
        if (this.factors.containsKey(name)) {
            return this.factors.get(name)
                               .create(
                                       server,
                                       request
                               );
        }
        return null;
    }

    public void register(PacketFactor factor) {
        this.factors.put(
                factor.getName(),
                factor
        );
    }
}
