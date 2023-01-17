package com.github.cao.awa.apricot.database.simple.serial;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.database.*;
import com.github.cao.awa.apricot.mathematic.base.*;

import java.util.function.*;

@Synchronized
public class SerialLongKvDatabase extends ApricotDatabase<Long, Long> {
    private final SerialKvDatabase db;

    public SerialLongKvDatabase(String dbFile) {
        this.db = new SerialKvDatabase(
                dbFile,
                8
        );
    }

    @Override
    public void forEach(BiConsumer<Long, Long> action) {
        this.db.forEach((k, v) -> action.accept(
                k,
                Base256.longFromBuf(v)
        ));
    }

    @Override
    public void set(Long key, Long value) {
        this.db.set(
                key,
                Base256.longToBuf(value)
        );
    }

    @Override
    public Long get(Long key) {
        return Base256.longFromBuf(this.db.get(key));
    }

    public Long delete(Long key) {
        return Base256.longFromBuf(this.db.delete(key));
    }

    public void append(Long value) {
        this.db.append(Base256.longToBuf(value));
    }

    public void deletes(Long from, Long to) {
        this.db.deletes(
                from,
                to
        );
    }
}
