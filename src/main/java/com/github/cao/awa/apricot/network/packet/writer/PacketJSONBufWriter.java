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
    private @NotNull JSONObject json;

    public PacketJSONBufWriter(@NotNull ApricotServer server, @NotNull Channel channel) {
        this.server = server;
        this.channel = channel;
        this.json = new JSONObject();
    }

    public JSONObject take() {
        return this.json;
    }

    public JSONObject take(String key) {
        if (! this.json.containsKey(key)) {
            this.json.put(
                    key,
                    new JSONObject()
            );
        }
        return this.json.getJSONObject(key);
    }

    public void done() {
        if (this.json.size() == 0) {
            LOGGER.trace("Has an occurs not completed writing, ignored");
            return;
        }
        String information = this.json.toString();
        TextWebSocketFrame frame = new TextWebSocketFrame(information);
        this.server.getTrafficsCounter().out(information.length());
        this.server.getPacketsCounter().out(1);
        this.channel.writeAndFlush(frame);
        flush();
    }

    public void flush() {
        this.json = new JSONObject();
    }
}
