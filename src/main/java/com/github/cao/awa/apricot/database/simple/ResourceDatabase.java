package com.github.cao.awa.apricot.database.simple;

import com.github.cao.awa.apricot.database.*;
import com.github.cao.awa.apricot.mathematic.base.*;
import org.iq80.leveldb.*;

import java.nio.charset.*;
import java.util.function.*;

public class ResourceDatabase extends ApricotDatabase<String, Long> {
    private final DB head;
    private final DB convert;

    public ResourceDatabase(DB head, DB convert) {
        this.head = head;
        this.convert = convert;
    }

    @Override
    public void forEach(BiConsumer<String, Long> action) {
        this.head.forEach(entry -> {
            action.accept(
                    new String(
                            entry.getKey(),
                            StandardCharsets.UTF_8
                    ),
                    Base256.longFromBuf(entry.getValue())
            );
        });
    }

    @Override
    public void set(String key, Long value) {
        this.head.put(
                key.getBytes(StandardCharsets.UTF_8),
                Base256.longToBuf(value)
        );
        this.convert.put(
                Base256.longToBuf(value),
                key.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public Long get(String key) {
        try {
            return Base256.longFromBuf(this.head.get(key.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            return - 1L;
        }
    }

    @Override
    public Long delete(String key) {
        Long result = get(key);
        this.head.delete(key.getBytes(StandardCharsets.UTF_8));
        return result;
    }

    public String getConvert(Long convert) {
        try {
            return new String(
                    this.convert.get(Base256.longToBuf(convert)),
                    StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            return "";
        }
    }
}
