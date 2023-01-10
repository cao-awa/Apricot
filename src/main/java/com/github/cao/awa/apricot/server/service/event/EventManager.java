package com.github.cao.awa.apricot.server.service.event;

import com.github.cao.awa.apricot.event.receive.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.server.service.*;
import com.github.cao.awa.apricot.server.service.plugin.*;
import com.github.cao.awa.apricot.thread.pool.*;

import java.util.concurrent.*;

public class EventManager implements ConcurrentService {
    private final ApricotServer server;
    private final ExecutorEntrust executor;
    private final PluginManager plugins;
    private boolean active = true;

    public EventManager(ApricotServer server, Executor executor, PluginManager plugins) {
        this.server = server;
        this.executor = new ExecutorEntrust(executor);
        this.plugins = plugins;
    }

    public boolean isActive() {
        return this.active;
    }

    public ApricotServer getServer() {
        return this.server;
    }

    public void fireEvent(Event<?> event) {
        if (this.active) {
            this.executor.execute(
                    "EventManager",
                    () -> this.plugins.getPlugins()
                                      .forEach(plugin -> this.executor.execute(
                                              "EventManager",
                                              () -> plugin.fireEvent(event)
                                      ))
            );
        }
    }

    @Override
    public void shutdown() {
        this.active = false;
        if (this.executor.getExecutor() instanceof ThreadPoolExecutor threadPool) {
            threadPool.shutdown();
        }
    }
}
