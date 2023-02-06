package com.github.cao.awa.apricot.server.service.task;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.server.service.*;
import com.github.cao.awa.apricot.thread.pool.*;

import java.util.concurrent.*;
import java.util.function.*;

@Stable
public class TaskManager implements ConcurrentService {
    private final ExecutorEntrust executor;
    private final ExecutorEntrust scheduler;

    public TaskManager(ExecutorEntrust executor, ExecutorEntrust scheduler) {
        this.executor = executor;
        this.scheduler = scheduler == null ? executor : scheduler;
    }

    public void execute(Runnable runnable) {
        this.executor.execute(
                runnable
        );
    }

    public void schedule(long delay, long interval, TimeUnit unit, Runnable command) {
        this.scheduler.schedule(
                delay,
                interval,
                unit,
                command
        );
    }

    public void schedule(long delay, TimeUnit unit, Runnable command) {
        this.scheduler.schedule(
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
