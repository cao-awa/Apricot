package com.github.cao.awa.bot.network.lazy;

import com.google.common.base.*;
import org.jetbrains.annotations.*;

import java.util.function.Supplier;

public class Lazy<T> {
    private final @NotNull Supplier<T> supplier;

    public Lazy(@NotNull Supplier<T> delegate) {
        this.supplier = Suppliers.memoize(delegate::get);
    }

    public T get() {
        return this.supplier.get();
    }
}

