package com.github.cao.awa.apricot.clock;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.utils.times.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle.*;

import java.util.concurrent.*;

/**
 * The low precision time clock.<br>
 * <br>
 * Response speed got maximum improvement, but precision got much reduced.<br>
 * {@link LowPrecisionClock#millions0() LowPrecisionClock.millions()} has 13x faster and 2x~3x precision loss than {@link System#currentTimeMillis() System.currentTimeMillis()}. <br>
 * <br>
 * Only use in case of no precision requires.<br>
 *
 * @author 草二号机
 * @since 1.0.0
 */
@Warning("LOW_PRECISION")
public final class LowPrecisionClock {
    private static final LowPrecisionClock clock = new LowPrecisionClock();
    private final ScheduledExecutorService executor;
    private final Receptacle<Long> millis;

    private LowPrecisionClock() {
        this.millis = Receptacle.of(TimeUtil.millions());
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.executor.scheduleAtFixedRate(
                () -> this.millis.set(TimeUtil.millions()),
                1,
                1,
                TimeUnit.MILLISECONDS
        );
    }

    private void shutdown() {
        this.executor.shutdown();
    }

    private long millions0() {
        return this.millis.get();
    }

    public static long millions() {
        return clock.millions0();
    }
}
