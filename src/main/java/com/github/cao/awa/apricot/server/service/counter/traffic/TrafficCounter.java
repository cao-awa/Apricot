package com.github.cao.awa.apricot.server.service.counter.traffic;

import com.github.cao.awa.apricot.clock.*;
import com.github.cao.awa.apricot.server.service.*;
import com.github.cao.awa.apricot.utils.times.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle.*;
import org.apache.logging.log4j.*;

import java.util.*;
import java.util.concurrent.*;

public class TrafficCounter implements ConcurrentService {
    private static final Logger LOGGER = LogManager.getLogger("TrafficCounter");
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final List<Traffic> traffics = new CopyOnWriteArrayList<>();
    private String name;
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

    public static void main(String[] args) {
//        TrafficCounter traffic = new TrafficCounter(10000);
//        Random random = new Random();
//        while (true) {
//            traffic.in(random.nextInt(
//                    0,
//                    200
//            ));
//            traffic.out(random.nextInt(
//                    0,
//                    200
//            ));
//            TimeUtil.coma(random.nextInt(
//                    0,
//                    10
//            ));
//        }
    }

    public void in(long in) {
        this.traffics.add(new Traffic(
                TimeUtil.millions(),
                in,
                true
        ));
    }

    public void out(long out) {
        this.traffics.add(new Traffic(
                TimeUtil.millions(),
                out,
                false
        ));
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
