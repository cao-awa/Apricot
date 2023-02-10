package com.github.cao.awa.apricot.network.packet.writer;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.server.*;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

@Stable
public class PacketJSONBufWriter {
    private static final Logger LOGGER = LogManager.getLogger("PacketWriter");
    private static final @NotNull ThreadLocal<JSONObject> JSONS = new ThreadLocal<>();
    private final @NotNull ApricotServer server;
    private final @NotNull Channel channel;

    public PacketJSONBufWriter(@NotNull ApricotServer server, @NotNull Channel channel) {
        this.server = server;
        this.channel = channel;
    }

    public JSONObject take() {
        return ensure();
    }

    private JSONObject ensure() {
        JSONObject json = JSONS.get();
        if (json == null) {
            json = new JSONObject();
            JSONS.set(json);
        }
        return json;
    }

    public JSONObject take(String key) {
        JSONObject json = ensure();
        JSONObject result;
        if (json.containsKey(key)) {
            result = json.getJSONObject(key);
        } else {
            result = new JSONObject();
            json.put(
                    key,
                    result
            );
        }
        return result;
    }

    public void done() {
        final JSONObject json = JSONS.get();
        if (json.size() == 0) {
            LOGGER.trace("Has an occurs not completed writing, ignored");
            return;
        }
        final String information = json.toString();
        this.server.getTrafficsCounter()
                   .out(information.length());
        this.server.getPacketsCounter()
                   .out(1);
        this.channel.writeAndFlush(new TextWebSocketFrame(information));
        flush();
    }

    public void flush() {
        JSONS.remove();
    }
}
