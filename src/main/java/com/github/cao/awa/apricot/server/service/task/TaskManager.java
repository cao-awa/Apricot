package com.github.cao.awa.apricot.server.service.task;

import com.github.cao.awa.apricot.server.service.*;
import com.github.cao.awa.apricot.thread.pool.*;

import java.util.concurrent.*;
import java.util.function.*;

public class TaskManager implements ConcurrentService {
    private final ExecutorEntrust executor;
    private final ExecutorEntrust scheduler;

    public TaskManager(ExecutorEntrust executor, ExecutorEntrust scheduler) {
        this.executor = executor;
        this.scheduler = scheduler == null ? executor : scheduler;
    }

    public void execute(String name, Runnable runnable) {
        this.executor.execute(
                name,
                runnable
        );
    }

    public void schedule(String entrust, long delay, long interval, TimeUnit unit, Runnable command) {
        this.scheduler.schedule(
                entrust,
                delay,
                interval,
                unit,
                command
        );
    }

    public void schedule(String entrust, long delay, TimeUnit unit, Runnable command) {
        this.scheduler.schedule(
                entrust,
                delay,
                unit,
                command
        );
    }

    public <T> CompletableFuture<T> future(Supplier<T> runnable) {
        return CompletableFuture.supplyAsync(
                runnable,
                this.executor
        );
    }

    @Override
    public void shutdown() {
        this.executor.shutdown();
        this.scheduler.shutdown();
    }
}
