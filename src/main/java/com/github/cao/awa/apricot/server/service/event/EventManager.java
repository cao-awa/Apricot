package com.github.cao.awa.apricot.server.service.event;

import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.immigration.illegal.*;
import com.github.cao.awa.apricot.plugin.accomplish.*;
import com.github.cao.awa.apricot.plugin.firewall.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.server.service.*;

import java.util.*;
import java.util.concurrent.*;

public class EventManager implements ConcurrentService {
    private final ApricotServer server;
    private final Executor executor;
    private final Map<UUID, AccomplishPlugin> plugins;
    private final Map<UUID, FirewallPlugin> firewalls;

    public EventManager(ApricotServer server, Executor executor, Map<UUID, AccomplishPlugin> plugins, Map<UUID, FirewallPlugin> firewalls) {
        this.server = server;
        this.executor = executor;
        this.plugins = plugins;
        this.firewalls = firewalls;
    }

    public void fireEvent(Event<?> event) {
        this.executor.execute(() -> {
            if (this.firewalls.values()
                              .stream()
                              .allMatch(firewall -> firewall.fireEvent(event))) {
                this.plugins.values()
                            .forEach(plugin -> this.executor.execute(() -> plugin.fireEvent(event)));
            } else {
                this.server.fireEvent(new IllegalImmigrationEvent(
                                    event.getProxy(),
                                    event
                            ));
            }
        });


    }

    @Override
    public void shutdown() {
        if (this.executor instanceof ThreadPoolExecutor threadPool) {
            threadPool.shutdown();
        }
    }
}
