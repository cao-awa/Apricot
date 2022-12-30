package com.github.cao.awa.apricot.network.packet;

import com.github.cao.awa.apricot.network.router.*;
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
