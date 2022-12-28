package com.github.cao.awa.apricot.comparative.tester;

import com.github.cao.awa.apricot.utils.times.*;
import org.apache.logging.log4j.*;

public class BenchmarkTester implements Tester {
    private static final Logger LOGGER = LogManager.getLogger("BenchmarkTester");
    private final Runnable[] tests;
    private final long times;

    public BenchmarkTester( long times, Runnable... tests) {
        this.tests = tests;
        this.times = times;
    }

    public static BenchmarkTester of(long times, Runnable... tests) {
        return new BenchmarkTester(times, tests);
    }

    public static void start(long times, Runnable... tests) {
        new BenchmarkTester(times, tests).start();
    }

    public void start() {
        LOGGER.info("Preparing run for all test");

        for (Runnable runnable : tests) {
            runnable.run();
        }

        LOGGER.info("Start test");

        long overallStart = TimeUtil.nano();
        for (int i = 0;i < tests.length;i++) {
            Runnable test = tests[i];
            long startOne = TimeUtil.nano();
            for (long times = this.times; times > 0; times--) {
                test.run();
            }
            long doneOne = TimeUtil.processNano(startOne);
            LOGGER.info("Done for test " + i + ", take " + doneOne + " ns (" + ((doneOne / 1000000)) + "ms)");
        }
        overallStart = TimeUtil.processNano(overallStart);
        LOGGER.info("Done for all test, take " + overallStart + " ns (" + ((overallStart / 1000000)) + "ms)");
    }
}
