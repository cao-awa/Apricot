package com.github.cao.awa.apricot.event;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.network.handler.*;

public abstract class Event {
    private final ApricotProxy proxy;

    public Event(ApricotProxy handler) {
        this.proxy = handler;
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
     * @param handler handler
     *
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    public abstract void entrust(EventHandler handler);
}
