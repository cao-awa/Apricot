package com.github.cao.awa.apricot.network.packet.receive.meta.lifecycle;

import com.github.cao.awa.apricot.event.receive.lifecycle.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.target.*;

public class ProxyDisconnectPacket extends ReadonlyPacket {
    private final String disconnectReason;
    private final long id;
    private final long timestamp;

    public ProxyDisconnectPacket(String disconnectReason, long id, long timestamp) {
        this.disconnectReason = disconnectReason;
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getDisconnectReason() {
        return this.disconnectReason;
    }

    public long getId() {
        return this.id;
    }

    public long getTimestamp() {
        return this.timestamp;
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
    @Override
    public void fireEvent(ApricotServer server, ApricotProxy proxy) {
        server.fireEvent(new ProxyDisconnectEvent(
                proxy,
                this
        ));
    }

    @Override
    public EventTarget target() {
        return new EventTarget(-1, -1, this.id);
    }
}
