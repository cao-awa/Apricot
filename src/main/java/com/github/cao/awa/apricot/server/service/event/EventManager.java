package com.github.cao.awa.apricot.server.service.event;

import com.github.cao.awa.apricot.event.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.server.service.*;

import java.util.*;
import java.util.concurrent.*;

public class EventManager implements ConcurrentService {
    private final Executor executor;
    private final Map<UUID, Plugin> plugins;

    public EventManager(Executor executor, Map<UUID, Plugin> plugins) {
        this.executor = executor;
        this.plugins = plugins;
    }

    public void fireEvent(Event<?> event) {
        this.plugins.values()
                    .forEach(plugin -> this.executor.execute(() -> plugin.fireEvent(event)));
    }

    @Override
    public void shutdown() {
        if (this.executor instanceof ThreadPoolExecutor threadPool) {
            threadPool.shutdown();
        }
    }
}
