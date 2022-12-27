package com.github.cao.awa.bot.event;

import com.github.cao.awa.bot.event.handler.*;
import com.github.cao.awa.bot.network.handler.*;

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

    public abstract void entrust(EventHandler handler);
}
