package com.github.cao.awa.apricot.server.service.counter.traffic;

import com.github.cao.awa.apricot.clock.*;
import com.github.cao.awa.apricot.server.service.*;
import com.github.cao.awa.apricot.util.time.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle.*;
import org.apache.logging.log4j.*;

import java.util.*;
import java.util.concurrent.*;

public class TrafficCounter implements ConcurrentService {
    private static final Logger LOGGER = LogManager.getLogger("TrafficCounter");
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final List<Traffic> traffics = new CopyOnWriteArrayList<>();
    private final String name;
    private long totalIn = 0;
    private long totalOut = 0;
    private long current = 0;
    private int interval = 1000;

    public TrafficCounter(String name, int interval) {
        this.name = name;
        this.interval = interval;
        update();
    }

    public TrafficCounter(String name) {
        this.name = name;
        update();
    }

    public long getTotalIn() {
        return this.totalIn;
    }

    public long getTotalOut() {
        return this.totalOut;
    }

    public void in(long in) {
        this.traffics.add(new Traffic(
                TimeUtil.millions(),
                in,
                true
        ));
        this.totalIn += in;
    }

    public void out(long out) {
        this.traffics.add(new Traffic(
                TimeUtil.millions(),
                out,
                false
        ));
        this.totalOut += out;
    }

    public Legacy<Long, Long> current() {
        long in = 0;
        long out = 0;
        for (Traffic traffic : this.traffics.stream()
                                            .filter(traffic -> this.current - traffic.key() < this.interval)
                                            .toList()) {
            if (traffic.isIn()) {
                in += traffic.value();
            } else {
                out += traffic.value();
            }
        }
        return new Legacy<>(
                in,
                out
        );
    }

    private void update() {
        //        Legacy<Long, Long> result = current();
        //
        //        LOGGER.info("Counter '{}' received {} in counts and {} out counts", this.name, result.newly(), result.stale());

        this.current = LowPrecisionClock.millions();

        this.traffics.stream()
                     .filter(traffic -> this.current - traffic.key() > this.interval)
                     .forEach(this.traffics::remove);

        this.executor.schedule(
                this::update,
                1,
                TimeUnit.SECONDS
        );
    }

    @Override
    public void shutdown() {
        this.executor.shutdown();
    }

    public record Traffic(long key, long value, boolean isIn) {
    }
}
