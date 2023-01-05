package com.github.cao.awa.apricot.database.message.store;

import com.github.cao.awa.apricot.database.*;
import org.iq80.leveldb.*;

import java.nio.charset.*;
import java.util.function.*;

public class MessageDatabase extends ApricotDatabase<String, String> {
    private final DB db;

    public MessageDatabase(DB db) {
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

    public void put(String key, String value) {
        this.db.put(
                key.getBytes(StandardCharsets.UTF_8),
                value.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String get(String key) {
        return new String(
                this.db.get(key.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8
        );
    }
}
