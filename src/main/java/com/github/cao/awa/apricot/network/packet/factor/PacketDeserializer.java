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
        try {
            String name;
            PacketFactor factor = null;
            if (request.containsKey("post_type")) {
                name = request.getString("post_type")
                              .replace(
                                      "_",
                                      "-"
                              );
                String subtype = request.containsKey("sub_type") ?
                                 request.getString("sub_type")
                                        .replace(
                                                "_",
                                                "-"
                                        ) :
                                 null;
                if ("message".equals(name)) {
                    factor = handleMessagePost(
                            request.getString("message_type"),
                            subtype
                    );
                } else if ("notice".equals(name)) {
                    factor = handleNoticePost(
                            request.getString("notice_type"),
                            subtype
                    );
                } else {
                    name = request.getString("post_type") + (subtype == null ? "" : "-" + subtype);
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

    private PacketFactor handleNoticePost(String noticeType, @Nullable String subType) {
        String name = "notice-" + noticeType.replace(
                "_",
                "-"
        ) + (subType == null ? "" : ("-" + subType));
        return this.factors.get(name);
    }

    public void register(PacketFactor factor) {
        this.factors.put(
                factor.getName(),
                factor
        );
    }
}
