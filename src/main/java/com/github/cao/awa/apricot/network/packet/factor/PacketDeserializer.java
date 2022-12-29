package com.github.cao.awa.apricot.network.packet.factor;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.invalid.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.collection.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PacketDeserializer {
    private final Map<String, PacketFactor> factors = ApricotCollectionFactor.newHashMap();

    @NotNull
    public ReadonlyPacket deserializer(ApricotServer server, JSONObject request) {
        String name;
        try {
            if (request.containsKey("post_type")) {
                name = request.getString("post_type");
            } else {
                request = request.getJSONObject("echo");
                name = request.getString("type");
            }
            PacketFactor factor = this.factors.containsKey(name) ?
                                  this.factors.get(name) :
                                  this.factors.get("invalid-data");
            return factor.create(
                    server,
                    request
            );
        } catch (Exception e) {
            return new InvalidDataReceivedPacket(
                    request,
                    true
            );
        }
    }

    public void register(PacketFactor factor) {
        this.factors.put(
                factor.getName(),
                factor
        );
    }
}
