package com.github.cao.awa.apricot.network.packet;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.writer.*;

public abstract class WritablePacket extends Packet {
    /**
     * Write data to buffer and flush the buffer.
     *
     * @param writer
     *         writer
     * @author 草二号机
     * @since 1.0.0
     */
    public void writeAndFlush(PacketJSONBufWriter writer) {
        this.write(writer);
        this.flush(writer);
    }

    /**
     * Write data to buffer.
     *
     * @param writer
     *         writer
     * @author cao_awa
     * @since 1.0.0
     */
    public abstract void write(PacketJSONBufWriter writer);

    /**
     * Flush buffer data, write it and reset buffer.
     *
     * @param writer
     *         writer
     * @author 草二号机
     * @author cao_aw
     * @since 1.0.0
     */
    public void flush(PacketJSONBufWriter writer) {
        if (shouldEcho()) {
            writer.take()
                  .putIfAbsent(
                          "echo",
                          new JSONObject().fluentPut(
                                                  "id",
                                                  getIdentifier()
                                          )
                                          .fluentPut(
                                                  "type",
                                                  "echo-result"
                                          )
                                          .fluentPut(
                                                  "response-type",
                                                  writer.take()
                                                        .getString("action")
                                          )
                  );
        }
        writer.done();
    }

    public abstract boolean shouldEcho();
}
