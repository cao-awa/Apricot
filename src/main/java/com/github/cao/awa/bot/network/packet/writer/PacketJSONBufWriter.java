package com.github.cao.awa.bot.network.packet.writer;

import com.alibaba.fastjson2.*;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

public class PacketJSONBufWriter {
    private static final Logger LOGGER = LogManager.getLogger("PacketWriter");
    private final @NotNull Channel channel;
    private @NotNull JSONObject json;

    public PacketJSONBufWriter(@NotNull Channel channel) {
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
        this.channel.writeAndFlush(new TextWebSocketFrame(this.json.toString()));
        create();
    }

    public void create() {
        this.json = new JSONObject();
    }
}
