package com.github.cao.awa.apricot.plugin.internal.bus.handlers.message;

import com.github.cao.awa.apricot.event.handler.message.recall.*;
import com.github.cao.awa.apricot.event.receive.message.recall.*;
import com.github.cao.awa.apricot.plugin.internal.bus.handlers.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.collection.*;

import java.util.*;
import java.util.function.*;

public class RecallBus extends MessageRecallEventHandler implements BusHandler<MessageRecallEvent<?>> {
    private final List<Consumer<MessageRecallEvent<?>>> handlers = ApricotCollectionFactor.newArrayList();

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
    public void onRecall(MessageRecallEvent<?> event) {
        ApricotServer server = event.proxy()
                                    .server();
        this.handlers.forEach(handler -> server.execute(() -> handler.accept(event)));
    }

    @Override
    public void register(Consumer<? extends MessageRecallEvent<?>> action) {
        this.handlers.add((Consumer<MessageRecallEvent<?>>) action);
    }

    @Override
    public void unregister(Consumer<? extends MessageRecallEvent<?>> action) {
        this.handlers.remove(action);
    }
}
