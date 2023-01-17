package com.github.cao.awa.apricot.plugin.internal.simple.handlers;

import com.github.cao.awa.apricot.event.receive.*;

import java.util.function.*;

public interface BusHandler<T extends Event<?>> {
    void register(Consumer<? extends T> action);

    void unregister(Consumer<? extends T> action);
}
