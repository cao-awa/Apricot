package com.github.cao.awa.bot.network.packet;

import com.github.cao.awa.bot.network.handler.*;
import com.github.cao.awa.bot.network.packet.writer.*;
import com.github.cao.awa.bot.server.*;

/**
 * Deserialized packet, unable to rewrite.
 * <br>
 * see {@link Packet}
 *
 * @since 1.0.0
 * @author cao_awa
 * @author 草二号机
 */
public abstract class ReadonlyPacket extends Packet {
    @Override
    public void write(PacketJSONBufWriter writer) {
        // Receiver packet do not write anything.
    }

    @Override
    public void writeAndFlush(PacketJSONBufWriter writer) {
        // Receiver packet do not write anything.
    }

    public abstract void fireEvent(ApricotServer server, ApricotProxy proxy);
}
