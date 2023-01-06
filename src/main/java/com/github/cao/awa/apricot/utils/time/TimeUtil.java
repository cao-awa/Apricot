package com.github.cao.awa.apricot.utils.time;

import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

public class TimeUtil {
    public static long millions() {
        return System.currentTimeMillis();
    }

    public static long nano() {
        return System.nanoTime();
    }

    public static long processMillion(long million) {
        return millions() - million;
    }

    public static long processNano(long nano) {
        return nano() - nano;
    }

    public static void sleep(long millions) throws InterruptedException {
        if (millions < 0)
            return;
        Thread.sleep(millions);
    }

    public static void coma(long millions) {
        EntrustEnvironment.trys(() -> Thread.sleep(millions));
    }

    public static long nanoId() {
        return nano() / 100;
    }
}
