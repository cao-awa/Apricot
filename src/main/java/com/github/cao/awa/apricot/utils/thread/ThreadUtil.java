package com.github.cao.awa.apricot.utils.thread;

public class ThreadUtil {
    public static void setName(String name) {
        setName(Thread.currentThread(), name);
    }

    public static void setName(Thread thread, String name) {
        thread.setName(name);
    }
}
