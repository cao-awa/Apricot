package com.github.cao.awa.apricot.plugin.firewall;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.utils.collection.*;

import java.util.*;

public abstract class FirewallPlugin extends Plugin {
    private final List<FirewallEventHandler> handlers = ApricotCollectionFactor.newArrayList();

    public void registerHandlers(FirewallEventHandler handler, FirewallEventHandler... handlers) {
        registerHandler(handler);
        for (FirewallEventHandler eventHandler : handlers) {
            registerHandler(eventHandler);
        }
    }

    public void registerHandler(FirewallEventHandler handler) {
        if (! this.handlers.contains(handler)) {
            this.handlers.add(handler);
        }
    }

    /**
     * Let an event be fired.
     *
     * @param event
     *         event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    public boolean fireEvent(Event<?> event) {
        return this.handlers.stream()
                            .allMatch(event::fireFirewall);
    }

    @Override
    public boolean shouldAsync() {
        return false;
    }
}
