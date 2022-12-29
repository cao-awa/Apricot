package com.github.cao.awa.apricot.plugin.accomplish;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.plugin.*;
import it.unimi.dsi.fastutil.objects.*;

import java.util.*;

public abstract class AccomplishPlugin extends Plugin {
    private final Map<String, List<AccomplishEventHandler>> handlers = new Object2ObjectOpenHashMap<>();

    public void registerHandlers(AccomplishEventHandler handler, AccomplishEventHandler... handlers) {
        registerHandler(handler);
        for (AccomplishEventHandler eventHandler : handlers) {
            registerHandler(eventHandler);
        }
    }

    public void registerHandler(AccomplishEventHandler handler) {
        if (! this.handlers.containsKey(handler.getName())) {
            this.handlers.put(
                    handler.getName(),
                    new LinkedList<>()
            );
        }
        this.handlers.get(handler.getName())
                     .add(handler);
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
    public void fireEvent(Event<?> event) {
        List<AccomplishEventHandler> handlers = this.handlers.get(event.getName());
        if (handlers != null) {
            handlers.forEach(event::fireAccomplish);
        }
    }

    @Override
    public boolean shouldAsync() {
        return true;
    }
}
