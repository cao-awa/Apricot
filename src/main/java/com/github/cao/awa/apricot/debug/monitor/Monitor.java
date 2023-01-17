package com.github.cao.awa.apricot.debug.monitor;

import com.github.cao.awa.apricot.util.collection.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class Monitor {
    private static final @NotNull ThreadLocal<String> ADAPTER = new ThreadLocal<>();
    private final @NotNull Map<String, Logger> listens = ApricotCollectionFactor.newConcurrentHashMap();
    private @NotNull String listen = "";

    public void push(@NotNull String info) {
        String name = ADAPTER.get();
        if (name == null) {
            throw new IllegalStateException("Monitor are not swapped");
        }
        push(
                name,
                info
        );
    }

    public void push(@NotNull String name, @NotNull String info) {
        this.notice(
                name,
                info
        );
    }

    private void notice(@NotNull String name, @NotNull String info) {
        if (name.equals(this.listen) || this.listen.equals("")) {
            if (! this.listens.containsKey(name)) {
                this.listens.put(
                        name,
                        LogManager.getLogger(name)
                );
            }
            this.listens.get(name)
                        .debug(info);
        }
    }

    public void push(@NotNull String info, Throwable throwable) {
        String name = ADAPTER.get();
        if (name == null) {
            throw new IllegalStateException("Monitor are not swapped");
        }
        push(
                name,
                info,
                throwable
        );
    }

    public void push(@NotNull String name, @NotNull String info, Throwable throwable) {
        this.notice(
                name,
                info,
                throwable
        );
    }

    private void notice(@NotNull String name, @NotNull String info, Throwable throwable) {
        if (name.equals(this.listen) || this.listen.equals("")) {
            if (! this.listens.containsKey(name)) {
                this.listens.put(
                        name,
                        LogManager.getLogger(name)
                );
            }
            this.listens.get(name)
                        .debug(
                                info,
                                throwable
                        );
        }
    }

    public void swap(String name) {
        ADAPTER.set(name);
    }

    public void listen(@NotNull String name) {
        if (! this.listens.containsKey(name)) {
            this.listens.put(
                    name,
                    LogManager.getLogger(name)
            );
        }
        this.listen = name;
    }
}
