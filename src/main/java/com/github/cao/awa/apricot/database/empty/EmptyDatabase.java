package com.github.cao.awa.apricot.database.empty;

import com.github.cao.awa.apricot.database.*;

import java.util.function.*;

public class EmptyDatabase<K, V> extends ApricotDatabase<K, V> {
    @Override
    public void forEach(BiConsumer<K, V> action) {

    }

    @Override
    public void set(K key, V value) {

    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public V delete(K key) {
        return null;
    }
}
