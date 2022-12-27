package com.github.cao.awa.bot.network.packet;

import com.github.cao.awa.bot.network.packet.writer.*;

/**
 * Abstract packet, for read or write something data.
 *
 * @since 1.0.0
 * @author cao_awa
 * @author 草二号机
 */
public abstract class Packet {
    /**
     * Write data to buffer and flush the buffer.
     *
     * @param writer writer
     *
     * @since 1.0.0
     * @author 草二号机
     */
    public void writeAndFlush(PacketJSONBufWriter writer) {
        this.write(writer);
        this.flush(writer);
    }

    /**
     * Write data to buffer.
     *
     * @param writer
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract void write(PacketJSONBufWriter writer);

    /**
     * Flush buffer data, write it and reset buffer.
     *
     * @param writer writer
     *
     * @since 1.0.0
     * @author 草二号机
     * @author cao_aw
     */
    public void flush(PacketJSONBufWriter writer) {
        writer.done();
    }
}
