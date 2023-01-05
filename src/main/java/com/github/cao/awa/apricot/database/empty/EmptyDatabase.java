package com.github.cao.awa.apricot.database.empty;

import com.github.cao.awa.apricot.database.*;

import java.util.function.*;

public class EmptyDatabase extends ApricotDatabase<String, String> {
    @Override
    public void forEach(BiConsumer<String, String> action) {

    }

    @Override
    public void put(String key, String value) {

    }

    @Override
    public String get(String key) {
        return "";
    }
}
