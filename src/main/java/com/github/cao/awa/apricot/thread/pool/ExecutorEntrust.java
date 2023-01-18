package com.github.cao.awa.apricot.thread.pool;

import com.github.cao.awa.apricot.util.thread.*;
import com.github.cao.awa.apricot.util.time.*;
import org.jetbrains.annotations.*;

import java.util.concurrent.*;

public final class ExecutorEntrust implements Executor {
    private static final @NotNull ThreadLocal<String> last = ThreadLocal.withInitial(() -> "");
    private final @NotNull Executor executor;

    public ExecutorEntrust(@NotNull Executor executor) {
        this.executor = executor;
    }

    public void schedule(@NotNull String entrust, long delay, long interval, @NotNull TimeUnit unit, @NotNull Runnable command) {
        if (this.executor instanceof ScheduledExecutorService scheduled) {
            if (interval > 0) {
                scheduled.scheduleAtFixedRate(
                        () -> execute0(
                                entrust,
                                command
                        ),
                        delay,
                        interval,
                        unit
                );
            } else {
                scheduled.schedule(
                        () -> execute0(
                                entrust,
                                command
                        ),
                        delay,
                        unit
                );
            }
        } else {
            if (interval > 0) {
                throw new IllegalStateException("The executor is not supports to schedule");
            }
            schedule(
                    entrust,
                    delay,
                    unit,
                    command
            );
        }
    }

    private void execute0(@NotNull String entrust, @NotNull Runnable command) {
        if (! entrust.equals(last.get())) {
            ThreadUtil.setName(entrust);
            last.set(entrust);
        }
        command.run();
    }

    public void schedule(@NotNull String entrust, long delay, @NotNull TimeUnit unit, @NotNull Runnable command) {
        if (this.executor instanceof ScheduledExecutorService scheduled) {
            scheduled.schedule(
                    () -> execute0(
                            entrust,
                            command
                    ),
                    delay,
                    unit
            );
        } else {
            execute(
                    entrust,
                    () -> {
                        TimeUtil.coma(unit.convert(
                                delay,
                                TimeUnit.MILLISECONDS
                        ));
                        command.run();
                    }
            );
        }
    }

    public void execute(@NotNull String entrust, @NotNull Runnable command) {
        this.executor.execute(() -> execute0(
                entrust,
                command
        ));
    }

    public Executor executor() {
        return this.executor;
    }

    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *
     * @param command
     *         the runnable task
     * @throws RejectedExecutionException
     *         if this task cannot be
     *         accepted for execution
     * @throws NullPointerException
     *         if command is null
     */
    @Override
    public void execute(@NotNull Runnable command) {
        this.execute(
                "Anonymous",
                command
        );
    }

    public void shutdown() {
        if (this.executor instanceof ExecutorService service) {
            service.shutdown();
        }
    }
}
