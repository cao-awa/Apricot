package com.github.cao.awa.apricot.event.receive.accomplish;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.router.*;

import java.util.*;

public abstract class Event<T extends ReadonlyPacket> {
    private final ApricotProxy proxy;
    private final T packet;

    public Event(ApricotProxy proxy, T packet) {
        this.proxy = proxy;
        this.packet = packet;
    }

    public T getPacket() {
        return this.packet;
    }

    public ApricotProxy getProxy() {
        return this.proxy;
    }

    public String toString() {
        return pipeline() + "(" + super.toString() + ")";
    }

    public abstract Set<String> pipeline();

    /**
     * Fire event, let event entrust handler to process self.
     *
     * @param handler
     *         handler
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    public abstract void fireAccomplish(AccomplishEventHandler handler);

    /**
     * Fire event, let event entrust handler to process self.
     *
     * @param handler
     *         handler
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    public abstract boolean fireFirewall(FirewallEventHandler handler);
}
