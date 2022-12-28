package com.github.cao.awa.apricot.comparative.tester;

import com.github.cao.awa.apricot.utils.times.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class ComparativeTester<T> implements Tester {
    private static final Logger LOGGER = LogManager.getLogger("ComparativeTester");
    private final Supplier<T>[] tests;
    private final T[] expects;
    private final long times;

    public ComparativeTester( long times, Supplier<T> test1, T expect1, Supplier<T> test2, T expect2) {
        this.tests = new Supplier[2];
        this.expects = (T[]) new Object[2];
        this.times = times;
        this.tests[0] = test1;
        this.tests[1] = test2;
        this.expects[0] = expect1;
        this.expects[1] = expect2;
    }

    public static <T> ComparativeTester<T> of(long times, @NotNull Supplier<T> test1, @NotNull T expect1, @NotNull Supplier<T> test2, @NotNull T expect2) {
        return new ComparativeTester<>(times, test1, expect1, test2, expect2);
    }

    public void start() {
        long overallStart = 0;
        for (int i = 0;i < tests.length;i++) {
            Supplier<T> test = tests[i];
            for (long times = this.times; times > 0; times--) {
                long start = TimeUtil.nano();
                T result = test.get();
                long used = TimeUtil.processNano(start);
                overallStart += used;
                T expect = expects[i];

                if (Objects.equals(expect, result)) {
                    LOGGER.info("Failing for test " + i + ", because result not equals to expect");
                    break;
                }
            }
            LOGGER.info("Done for test " + i + ", take " + overallStart + " ns (" + ((overallStart / 1000000)) + "ms)");
        }
        LOGGER.info("Done for all test, take " + overallStart + " ns (" + ((overallStart / 1000000)) + "ms)");
    }
}
