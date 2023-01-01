package com.github.cao.awa.apricot.thread.pool;

import com.github.cao.awa.apricot.utils.thread.*;

import java.util.concurrent.*;

public class ExecutorEntrust {
    private final Executor executor;
    private String last = "";

    public ExecutorEntrust(Executor executor) {
        this.executor = executor;
    }

    public void execute(String entrust, Runnable command) {
        this.executor.execute(() -> {
            if (!entrust.equals(last)) {
                ThreadUtil.setName(entrust);
                this.last = entrust;
            }
            command.run();
        });
    }

    public Executor getExecutor() {
        return this.executor;
    }
}
