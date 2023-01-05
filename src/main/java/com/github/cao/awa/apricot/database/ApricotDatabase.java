package com.github.cao.awa.apricot.database;

import java.util.function.*;

public abstract class ApricotDatabase<K, V> {
    public abstract void forEach(BiConsumer<K, V> action);

    public abstract void put(K key, V value);

    public abstract V get(K key);
}
