package com.github.cao.awa.apricot.server.service.event;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.server.service.*;
import com.github.cao.awa.apricot.server.service.event.exclusive.*;
import com.github.cao.awa.apricot.server.service.plugin.*;
import com.github.cao.awa.apricot.target.*;
import com.github.cao.awa.apricot.thread.pool.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.cao.awa.apricot.util.time.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle.*;

import java.util.*;
import java.util.concurrent.*;

public class EventManager implements ConcurrentService {
    private final ApricotServer server;
    private final ExecutorEntrust executor;
    private final PluginManager plugins;
    private final Map<EventTarget, EventExclusive> exclusives = ApricotCollectionFactor.newConcurrentHashMap();
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
            final Map<EventTarget, EventExclusive> exclusives = new HashMap<>(this.exclusives);
            this.executor.execute(
                    "EventManager",
                    () -> {
                        for (Plugin plugin : this.plugins.getPlugins()) {
                            EventTarget target = event.getPacket()
                                                      .target();
                            EventExclusive exclusive = exclusives.get(target);
                            this.executor.execute(
                                    "EventManager",
                                    exclusive == null ? () -> plugin.fireEvent(event) : () -> {
                                        event.setExclusive(true);
                                        event.fireEvent(exclusive.handler());
                                    }
                            );
                            if (exclusive != null) {
                                exclusive.counts()
                                         .set(exclusive.counts()
                                                       .get() - 1);
                                if (exclusive.counts()
                                             .get() == 0) {
                                    this.exclusives.remove(target);
                                    exclusives.remove(target);
                                }
                                break;
                            }
                        }
                    }
            );
        }
    }

    public void exclusive(EventTarget target, EventHandler<?> handler) {
        if (this.exclusives.containsKey(target)) {
            this.exclusives.remove(target);
            throw new IllegalStateException("The exclusive for this target already registered, breaking this");
        }
        this.exclusives.put(
                target,
                new EventExclusive(
                        handler,
                        Receptacle.of(1)
                )
        );
    }

    public void exclusive(EventTarget target, EventHandler<?> handler, int counts) {
        if (this.exclusives.containsKey(target)) {
            this.exclusives.remove(target);
            throw new IllegalStateException("The exclusive for this target already registered, breaking this");
        }
        this.exclusives.put(
                target,
                new EventExclusive(
                        handler,
                        Receptacle.of(counts)
                )
        );
    }

    public void exclusive(EventTarget target, EventHandler<?> handler, int counts, long timeout) {
        if (this.exclusives.containsKey(target)) {
            this.exclusives.remove(target);
            throw new IllegalStateException("The exclusive for this target already registered, breaking this");
        }
        this.exclusives.put(
                target,
                new EventExclusive(
                        handler,
                        Receptacle.of(counts),
                        TimeUtil.millions(),
                        timeout,
                        () -> {
                        }
                )
        );
    }

    public void exclusive(EventTarget target, EventHandler<?> handler, int counts, long timeout, Runnable callback) {
        if (this.exclusives.containsKey(target)) {
            this.exclusives.remove(target);
            throw new IllegalStateException("The exclusive for this target already registered, breaking this");
        }
        long recorded = TimeUtil.millions();
        this.exclusives.put(
                target,
                new EventExclusive(
                        handler,
                        Receptacle.of(counts),
                        recorded,
                        timeout,
                        callback
                )
        );
        this.executor.schedule(
                "EventManager",
                timeout,
                TimeUnit.MILLISECONDS,
                () -> {
                    EventExclusive exclusive = this.exclusives.get(target);
                    if (exclusive == null) {
                        return;
                    }
                    if (exclusive.recorded() == recorded) {
                        this.exclusives.remove(target);
                        exclusive.timeoutCallback()
                                 .run();
                    }
                }
        );
    }

    @Override
    public void shutdown() {
        this.active = false;
        if (this.executor.executor() instanceof ThreadPoolExecutor threadPool) {
            threadPool.shutdown();
        }
    }
}
