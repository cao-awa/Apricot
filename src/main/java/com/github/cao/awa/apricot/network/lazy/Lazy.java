package com.github.cao.awa.apricot.network.lazy;

import com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle.*;
import org.jetbrains.annotations.*;

import java.util.function.Supplier;

/**
 * Memoize the supplier result to faster response get.
 *
 * @param <T> type
 *
 * @since 1.0.0
 * @author 草二号机
 */
public final class Lazy<T> {
    private final @NotNull Supplier<T> supplier;
    private final Receptacle<T> result = Receptacle.of();

    public Lazy(@NotNull Supplier<T> delegate) {
        this.supplier = delegate;
    }

    public T get() {
        return this.result.getOrSet(this.supplier);
    }
}

