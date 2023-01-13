package com.github.cao.awa.apricot.thread.pool;

import com.github.cao.awa.apricot.util.thread.*;
import com.github.cao.awa.apricot.util.time.*;

import java.util.concurrent.*;

public record ExecutorEntrust(Executor executor) {
    private static final ThreadLocal<String> last = ThreadLocal.withInitial(() -> "");

    public void schedule(String entrust, long delay, long interval, TimeUnit unit, Runnable command) {
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

    private void execute0(String entrust, Runnable command) {
        if (! entrust.equals(last.get())) {
            ThreadUtil.setName(entrust);
            last.set(entrust);
        }
        command.run();
    }

    public void schedule(String entrust, long delay, TimeUnit unit, Runnable command) {
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

    public void execute(String entrust, Runnable command) {
        this.executor.execute(() -> execute0(
                entrust,
                command
        ));
    }
}
