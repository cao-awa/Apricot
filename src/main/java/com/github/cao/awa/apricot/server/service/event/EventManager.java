package com.github.cao.awa.apricot.server.service.event;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.*;
import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.server.service.*;
import com.github.cao.awa.apricot.server.service.event.exclusive.*;
import com.github.cao.awa.apricot.server.service.plugin.*;
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
            final Map<EventTarget, EventExclusive> currentExclusives = ApricotCollectionFactor.newHashMap(this.exclusives);
            EventTarget target = event.getPacket()
                                      .target();
            EventExclusive exclusive = currentExclusives.get(target);
            this.executor.execute(
                    "EventManager",
                    () -> {
                        // Handle exclusive.
                        final boolean blocked = exclusive != null;
                        if (blocked) {
                            EventHandler<?> handler = exclusive.handler();

                            // Fire event for exclusive handler.
                            event.setExclusive(true);
                            event.fireEvent(handler);

                            // Count down the exclusive.
                            if (exclusive.counts()
                                         .set(exclusive.counts()
                                                       .get() - 1)
                                         .get() == 0) {
                                // Remove it when count to zero.
                                this.exclusives.remove(target);
                                currentExclusives.remove(target);
                            }
                        }

                        // Handle event.
                        this.plugins.getPlugins()
                                    .forEach(plugin -> this.executor.execute(
                                            "EventManager",
                                            () -> {
                                                // Handling event exclusive.
                                                if (blocked && exclusive.blocked(plugin)) {
                                                    // Fire event for compulsory handlers.
                                                    event.setExclusive(false);
                                                    plugin.fireEvent(
                                                            event,
                                                            exclusive.handler()
                                                    );

                                                    // Do not let event be fired again with normally.
                                                    return;
                                                }

                                                // Fire event normally.
                                                plugin.fireEvent(event);
                                            }
                                    ));
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

    public void exclusive(EventTarget target, EventHandler<?> handler, int counts, long timeout) {
        if (counts == - 1 && timeout == - 1) {
            throw new IllegalArgumentException("Must be timeout when exclusive request is unlimited");
        }

        if (timeout == 0) {
            return;
        }

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
                        },
                        EventExclusiveTarget.SELF
                )
        );
    }

    public void exclusive(EventTarget target, EventHandler<?> handler, int counts, long timeout, Runnable callback) {
        if (counts == - 1 && timeout == - 1) {
            throw new IllegalArgumentException("Must be timeout when exclusive request is unlimited");
        }

        if (timeout == 0) {
            callback.run();
            return;
        }

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
                        callback,
                        EventExclusiveTarget.SELF
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

    public void exclusive(EventTarget target, EventHandler<?> handler, int counts, long timeout, Runnable callback, EventExclusiveTarget exclusiveTarget) {
        if (counts == - 1 && timeout == - 1) {
            throw new IllegalArgumentException("Must be timeout when exclusive request is unlimited");
        }

        if (timeout == 0) {
            callback.run();
            return;
        }

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
                        callback,
                        exclusiveTarget
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

    public void release(EventTarget target) {
        this.exclusives.remove(target);
    }

    @Override
    public void shutdown() {
        this.active = false;
        this.executor.shutdown();
    }
}
