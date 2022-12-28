package com.github.cao.awa.apricot.network.packet;

import com.github.cao.awa.apricot.network.packet.writer.*;

/**
 * Abstract packet, for read or write something data.
 *
 * @author cao_awa
 * @author 草二号机
 * @since 1.0.0
 */
public abstract class Packet {
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
        writer.done();
    }
}
