package com.github.cao.awa.apricot.plugin.internal.bus.handlers.message;

import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.plugin.internal.bus.handlers.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.collection.*;

import java.util.*;
import java.util.function.*;

public class MessageBus extends MessageEventHandler implements BusHandler<MessageEvent<?>> {
    private final List<Consumer<MessageEvent<?>>> handlers = ApricotCollectionFactor.newArrayList();

    /**
     * Process event.
     *
     * @param event
     *         event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void onMessage(MessageEvent<?> event) {
        ApricotServer server = event.getProxy()
                                    .server();
        this.handlers.forEach(handler -> server.execute(() -> handler.accept(event)));
    }

    @Override
    public void register(Consumer<? extends MessageEvent<?>> action) {
        this.handlers.add((Consumer<MessageEvent<?>>) action);
    }

    @Override
    public void unregister(Consumer<? extends MessageEvent<?>> action) {
        this.handlers.remove(action);
    }
}
