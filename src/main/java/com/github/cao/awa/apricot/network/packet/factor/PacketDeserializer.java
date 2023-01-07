package com.github.cao.awa.apricot.network.packet.factor;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.invalid.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.cao.awa.apricot.utils.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PacketDeserializer {
    private final Map<String, PacketFactor> factors = ApricotCollectionFactor.newHashMap();

    @NotNull
    public ReadonlyPacket deserializerPacket(ApricotServer server, JSONObject request) {
        try {
            String name;
            PacketFactor factor = null;
            if (request.containsKey("post_type")) {
                String postType = TextUtil.underlineDash(request.getString("post_type"));
                name = postType;
                String subtype = request.containsKey("sub_type") ?
                                 TextUtil.underlineDash(request.getString("sub_type")) :
                                 null;
                switch (postType) {
                    case "message" -> {
                        String messageType = TextUtil.underlineDash(request.getString("message_type"));
                        factor = handleMessagePost(
                                messageType,
                                subtype
                        );
                    }
                    case "notice" -> {
                        String noticeType = TextUtil.underlineDash(request.getString("notice_type"));
                        factor = handleNoticePost(
                                noticeType,
                                subtype
                        );
                    }
                    case "meta-event" -> {
                        String metaEventType = TextUtil.underlineDash(request.getString("meta_event_type"));
                        factor = handleMetaEvent(
                                metaEventType,
                                subtype
                        );
                    }
                    case "request" -> {
                        String metaEventType = TextUtil.underlineDash(request.getString("request_type"));
                        factor = handleRequest(
                                metaEventType,
                                subtype
                        );
                    }
                    default -> name = postType + (subtype == null ? "" : "-" + subtype);
                }
            } else {
                name = request.getJSONObject("echo")
                              .getString("type");
            }
            if (factor == null) {
                factor = this.factors.getOrDefault(
                        name,
                        this.factors.get("invalid-data")
                );
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

    private PacketFactor handleMetaEvent(String metaEventType, @Nullable String subType) {
        String name = "meta-" + metaEventType.replace(
                "_",
                "-"
        ) + (subType == null ? "" : ("-" + subType));
        return this.factors.get(name);
    }

    private PacketFactor handleRequest(String metaEventType, @Nullable String subType) {
        String name = "request-" + metaEventType.replace(
                "_",
                "-"
        ) + (subType == null ? "" : ("-" + subType));
        return this.factors.get(name);
    }

    @Nullable
    @Unsupported
    public ReadonlyPacket deserializerResponse(ApricotServer server, String type, JSONObject request) {
        return this.factors.get(type)
                           .create(
                                   server,
                                   request
                           );
    }

    @NotNull
    public ReadonlyPacket deserializer(String name, ApricotServer server, JSONObject request) {
        return this.factors.getOrDefault(
                           name,
                           this.factors.get("invalid-data")
                   )
                           .create(
                                   server,
                                   request
                           );
    }

    public void register(PacketFactor factor) {
        this.factors.put(
                factor.getName(),
                factor
        );
    }
}
