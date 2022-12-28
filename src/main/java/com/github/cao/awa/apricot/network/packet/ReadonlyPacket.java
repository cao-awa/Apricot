package com.github.cao.awa.apricot.network.packet;

import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.server.*;

/**
 * Deserialized packet, unable to rewrite.
 * <br>
 * see {@link Packet}
 *
 * @author cao_awa
 * @author 草二号机
 * @since 1.0.0
 */
public abstract class ReadonlyPacket extends Packet {
    /**
     * Write data to buffer and flush the buffer.
     *
     * @param writer
     *         writer
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void writeAndFlush(PacketJSONBufWriter writer) {
        // Receiver packet do not write anything.
    }

    /**
     * Write data to buffer.
     *
     * @param writer
     *         writer
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public void write(PacketJSONBufWriter writer) {
        // Receiver packet do not write anything.
    }

    /**
     * Let an event of this packet be fired.
     *
     * @param server
     *         apricot server
     * @param proxy
     *         apricot proxy
     * @author 草二号机
     * @since 1.0.0
     */
    public abstract void fireEvent(ApricotServer server, ApricotProxy proxy);
}
