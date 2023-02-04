package com.github.cao.awa.apricot.server.service.event;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.*;
import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.plugin.*;
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
import java.util.function.*;

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
            this.executor.execute(
                    "EventManager",
                    () -> this.plugins.getPlugins()
                                      .forEach(plugin -> {
                                          EventTarget target = event.getPacket()
                                                                    .target();
                                          EventExclusive exclusive = currentExclusives.get(target);
                                          this.executor.execute(
                                                  "EventManager",
                                                  () -> {
                                                      // Handling event exclusive.
                                                      if (exclusive != null) {
                                                          EventHandler<?> handler = exclusive.handler();
                                                          // Level exclusives.
                                                          Predicate<Plugin> predicate = switch (exclusive.target()
                                                                                                         .getLevel()) {
                                                              case SELF -> p -> p == plugin;
                                                              case ALL -> p -> true;
                                                              case SPECIALS -> p -> exclusive.target()
                                                                                             .getTargets()
                                                                                             .contains(p);
                                                          };

                                                          // Test the predicate.
                                                          if (predicate.test(handler.getPlugin())) {
                                                              // Fire event for exclusive handler.
                                                              event.setExclusive(true);
                                                              event.fireEvent(handler);

                                                              // Fire event for compulsory handlers.
                                                              event.setExclusive(false);
                                                              plugin.fireEvent(
                                                                      event,
                                                                      handler
                                                              );

                                                              // Count down the exclusive.
                                                              if (exclusive.counts()
                                                                           .set(exclusive.counts()
                                                                                         .get() - 1)
                                                                           .get() == 0) {
                                                                  // Remove it when count to zero.
                                                                  this.exclusives.remove(target);
                                                                  currentExclusives.remove(target);
                                                              }

                                                              // Do not let event be fired again with normally.
                                                              return;
                                                          }
                                                      }

                                                      // Fire event normally.
                                                      plugin.fireEvent(event);
                                                  }
                                          );
                                      })
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
