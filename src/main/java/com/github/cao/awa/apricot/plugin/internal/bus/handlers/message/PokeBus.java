package com.github.cao.awa.apricot.plugin.internal.bus.handlers.message;

import com.github.cao.awa.apricot.event.handler.poke.*;
import com.github.cao.awa.apricot.event.receive.poke.*;
import com.github.cao.awa.apricot.plugin.internal.bus.handlers.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.collection.*;

import java.util.*;
import java.util.function.*;

public class PokeBus extends PokeReceivedEventHandler implements BusHandler<PokeReceivedEvent<?>> {
    private final List<Consumer<PokeReceivedEvent<?>>> handlers = ApricotCollectionFactor.newArrayList();

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
    public void onPoke(PokeReceivedEvent<?> event) {
        ApricotServer server = event.getProxy()
                                    .server();
        this.handlers.forEach(handler -> {
            server.execute(
                    getPlugin().getName(),
                    () -> handler.accept(event)
            );
        });
    }

    @Override
    public void register(Consumer<? extends PokeReceivedEvent<?>> action) {
        this.handlers.add((Consumer<PokeReceivedEvent<?>>) action);
    }

    @Override
    public void unregister(Consumer<? extends PokeReceivedEvent<?>> action) {
        this.handlers.remove(action);
    }
}
