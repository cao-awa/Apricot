package com.github.cao.awa.apricot.comparative.tester;

import com.github.cao.awa.apricot.utils.times.*;
import it.unimi.dsi.fastutil.*;
import org.apache.logging.log4j.*;

import java.util.function.*;

public class BenchmarkTesterWithParameters implements Tester {
    private static final Logger LOGGER = LogManager.getLogger("BenchmarkTester");
    private final Pair<Object, Consumer<Object>>[] tests;
    private final long times;

    @SafeVarargs
    public BenchmarkTesterWithParameters(long times, Pair<Object, Consumer<Object>>... tests) {
        this.tests = tests;
        this.times = times;
    }

    @SafeVarargs
    public static BenchmarkTesterWithParameters of(long times, Pair<Object, Consumer<Object>>... tests) {
        return new BenchmarkTesterWithParameters(times, tests);
    }

    @SafeVarargs
    public static void start(long times, Pair<Object, Consumer<Object>>... tests) {
        of(times, tests).start();
    }

    public void start() {
        LOGGER.info("Preparing run for all test");

        for (Pair<Object, Consumer<Object>> runnable : tests) {
            runnable.right().accept(runnable.left());
        }

        LOGGER.info("Start test");

        long overallStart = TimeUtil.nano();
        for (int i = 0;i < tests.length;i++) {
            Pair<Object, Consumer<Object>> test = tests[i];
            long startOne = TimeUtil.nano();
            for (long times = this.times; times > 0; times--) {
                test.right().accept(test.left());
            }
            long doneOne = TimeUtil.processNano(startOne);
            LOGGER.info("Done for test " + i + ", take " + doneOne + " ns (" + ((doneOne / 1000000)) + "ms)");
        }
        overallStart = TimeUtil.processNano(overallStart);
        LOGGER.info("Done for all test, take " + overallStart + " ns (" + ((overallStart / 1000000)) + "ms)");
    }
}
