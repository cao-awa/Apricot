package com.github.cao.awa.apricot.network.packet.writer;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.server.*;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

public class PacketJSONBufWriter {
    private static final Logger LOGGER = LogManager.getLogger("PacketWriter");
    private final @NotNull ApricotServer server;
    private final @NotNull Channel channel;
    private final @NotNull ThreadLocal<JSONObject> json;

    public PacketJSONBufWriter(@NotNull ApricotServer server, @NotNull Channel channel) {
        this.server = server;
        this.channel = channel;
        this.json = new ThreadLocal<>();
        this.json.set(new JSONObject());
    }

    public JSONObject take() {
        return this.json.get();
    }

    public JSONObject take(String key) {
        final JSONObject json = this.json.get();
        if (! json.containsKey(key)) {
            json.put(
                    key,
                    new JSONObject()
            );
        }
        return json.getJSONObject(key);
    }

    public void done() {
        final JSONObject json = this.json.get();
        if (json.size() == 0) {
            LOGGER.trace("Has an occurs not completed writing, ignored");
            return;
        }
        String information = json.toString();
        TextWebSocketFrame frame = new TextWebSocketFrame(information);
        this.server.getTrafficsCounter()
                   .out(information.length());
        this.server.getPacketsCounter()
                   .out(1);
        this.channel.writeAndFlush(frame);
        flush();
    }

    public void flush() {
        this.json.set(new JSONObject());
    }
}
