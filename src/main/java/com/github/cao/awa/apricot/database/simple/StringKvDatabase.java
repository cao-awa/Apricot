package com.github.cao.awa.apricot.database.simple;

import com.github.cao.awa.apricot.database.*;
import org.iq80.leveldb.*;

import java.nio.charset.*;
import java.util.function.*;

public class StringKvDatabase extends ApricotDatabase<String, String> {
    private static final byte[] EMPTY_BYTES = new byte[0];
    private final DB db;

    public StringKvDatabase(DB db) {
        this.db = db;
    }

    @Override
    public void forEach(BiConsumer<String, String> action) {
        this.db.forEach(entry -> {
            action.accept(
                    new String(
                            entry.getKey(),
                            StandardCharsets.UTF_8
                    ),
                    new String(
                            entry.getValue(),
                            StandardCharsets.UTF_8
                    )
            );
        });
    }

    @Override
    public void set(String key, String value) {
        this.db.put(
                key.getBytes(StandardCharsets.UTF_8),
                value.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public String get(String key) {
        return new String(
                get(key.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8
        );
    }

    public byte[] get(byte[] key) {
        byte[] bytes = this.db.get(key);
        return bytes == null ? EMPTY_BYTES : bytes;
    }

    @Override
    public String delete(String key) {
        byte[] delete = key.getBytes(StandardCharsets.UTF_8);
        String result = new String(this.db.get(delete));
        this.db.delete(delete);
        return result;
    }
}
