package com.github.cao.awa.apricot.thread.pool;

import com.github.cao.awa.apricot.util.thread.*;
import com.github.cao.awa.apricot.util.time.*;

import java.util.concurrent.*;

public class ExecutorEntrust {
    private final Executor executor;
    private String last = "";

    public ExecutorEntrust(Executor executor) {
        this.executor = executor;
    }

    public void execute(String entrust, Runnable command) {
        this.executor.execute(() -> execute0(entrust, command));
    }

    private void execute0(String entrust, Runnable command) {
        if (!entrust.equals(last)) {
            ThreadUtil.setName(entrust);
            this.last = entrust;
        }
        command.run();
    }

    public void schedule(String entrust, long delay, long interval, TimeUnit unit, Runnable command) {
        if (this.executor instanceof ScheduledExecutorService scheduled) {
            if (interval > 0) {
                scheduled.scheduleAtFixedRate(() -> execute0(entrust, command), delay, interval, unit);
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
            schedule(entrust, delay, unit, command);
        }
    }

    public void schedule(String entrust, long delay, TimeUnit unit, Runnable command) {
        if (this.executor instanceof ScheduledExecutorService scheduled) {
            scheduled.schedule(() -> execute0(entrust, command), delay, unit);
        } else {
            execute(entrust, () -> {
                TimeUtil.coma(unit.convert(delay, TimeUnit.MILLISECONDS));
                command.run();
            });
        }
    }

    public Executor getExecutor() {
        return this.executor;
    }
}
