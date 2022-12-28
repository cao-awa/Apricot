package com.github.cao.awa.apricot.event;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;

public abstract class Event<T extends ReadonlyPacket> {
    private final ApricotProxy proxy;
    private final T packet;

    public Event(ApricotProxy handler, T packet) {
        this.proxy = handler;
        this.packet = packet;
    }

    public T getPacket() {
        return packet;
    }

    public ApricotProxy getProxy() {
        return this.proxy;
    }

    public String toString() {
        return getName() + "(" + super.toString() + ")";
    }

    public abstract String getName();

    /**
     * Fire event, let event entrust handler to process self.
     *
     * @param handler
     *         handler
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    public abstract void entrust(EventHandler handler);
}
