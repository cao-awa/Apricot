package com.github.cao.awa.apricot.thread.pool;

import java.util.concurrent.*;

public class ExecutorFactor {
    public static ExecutorService virtual() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    public static ExecutorService cached() {
        return Executors.newCachedThreadPool();
    }

    public static ExecutorService fixed(int nThreads) {
        return Executors.newFixedThreadPool(nThreads);
    }

    public static ExecutorService single() {
        return Executors.newSingleThreadExecutor();
    }

    public static ExecutorService intensiveIo() {
        return virtual();
    }

    public static ExecutorService intensiveCpu() {
        return cached();
    }

    public static ExecutorService scheduled(int coreSize) {
        return Executors.newScheduledThreadPool(coreSize);
    }
}
