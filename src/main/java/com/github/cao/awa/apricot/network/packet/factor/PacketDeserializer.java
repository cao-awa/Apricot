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
            PacketFactor factor = null;
            if (request.containsKey("post_type")) {
                name = request.getString("post_type");
                if ("message".equals(name)) {
                    factor = handleMessagePost(
                            request.getString("message_type"),
                            request.getString("sub_type")
                    );
                } else {
                    name = request.getString("post_type") + "-" + request.getString("sub_type");
                }
            } else {
                request = request.getJSONObject("echo");
                name = request.getString("type");
            }
            if (factor == null) {
                factor = this.factors.containsKey(name) ? this.factors.get(name) : this.factors.get("invalid-data");
            }
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

    private PacketFactor handleMessagePost(String messageType, String subType) {
        String name = "message-" + messageType + "-" + subType;
        return this.factors.get(name);
    }

    public void register(PacketFactor factor) {
        this.factors.put(
                factor.getName(),
                factor
        );
    }
}
