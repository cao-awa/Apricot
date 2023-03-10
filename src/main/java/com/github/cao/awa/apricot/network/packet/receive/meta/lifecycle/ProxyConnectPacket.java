package com.github.cao.awa.apricot.network.packet.receive.meta.lifecycle;

import com.github.cao.awa.apricot.event.receive.lifecycle.*;
import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;

public class ProxyConnectPacket extends ReadonlyPacket {
    private final long id;
    private final long timestamp;

    public ProxyConnectPacket(long id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
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
        server.fireEvent(new ProxyConnectEvent(
                proxy,
                this
        ));
    }

    @Override
    public EventTarget target() {
        return new EventTarget(-1, -1, this.id);
    }
}
