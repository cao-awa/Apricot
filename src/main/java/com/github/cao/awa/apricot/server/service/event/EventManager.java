package com.github.cao.awa.apricot.server.service.event;

import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.immigration.illegal.*;
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

    public void fireEvent(Event<?> event) {
        if (this.active) {
            this.executor.execute(
                    "EventManager",
                    () -> {
                        if (this.plugins.getFirewallPlugins()
                                        .stream()
                                        .allMatch(firewall -> firewall.fireEvent(event))) {
                            this.plugins.getAccomplishPlugins()
                                        .forEach(plugin -> this.executor.execute(
                                                "EventManager",
                                                () -> plugin.fireEvent(event)
                                        ));
                        } else {
                            this.server.fireEvent(new IllegalImmigrationEvent(
                                    event.getProxy(),
                                    event
                            ));
                        }
                    }
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
