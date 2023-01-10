package com.github.cao.awa.apricot.database.message.store;

import com.github.cao.awa.apricot.database.*;
import com.github.cao.awa.apricot.mathematic.base.*;
import com.github.cao.awa.apricot.message.store.*;
import com.github.cao.awa.apricot.server.*;
import org.iq80.leveldb.*;

import java.nio.charset.*;
import java.util.function.*;

@Deprecated
public class MessageDatabase2 extends ApricotDatabase<Long, MessageStore> {
    private final ApricotServer server;
    private final DB head;
    private final DB convert;

    public MessageDatabase2(ApricotServer server, DB head, DB convert) {
        this.server = server;
        this.head = head;
        this.convert = convert;
    }

    @Override
    public void forEach(BiConsumer<Long, MessageStore> action) {
        this.head.forEach(entry -> {
            action.accept(
                    Base256.longFromBuf(entry.getKey()),
                    MessageStore.fromBin(
                            this.server,
                            entry.getValue()
                    )
            );
        });
    }

    @Override
    public void set(Long key, MessageStore value) {
        this.head.put(
                Base256.longToBuf(key),
                value.toJSONObject()
                     .toString()
                     .getBytes(StandardCharsets.UTF_8)
        );
        this.convert.put(
                Base256.intToBuf(value.getMessageId()),
                Base256.longToBuf(key)
        );
    }

    @Override
    public MessageStore get(Long key) {
        return MessageStore.fromBin(
                this.server,
                this.head.get(Base256.longToBuf(key))
        );
    }

    @Override
    public MessageStore delete(Long key) {
        MessageStore result = get(key);
        this.head.delete(Base256.longToBuf(key));
        return result;
    }

    public void setFromId(int id, MessageStore value) {
        set(
                getConvert(id),
                value
        );
    }

    public long getConvert(int id) {
        return Base256.longFromBuf(this.convert.get(Base256.intToBuf(id)));
    }

    public MessageStore getFromId(int id) {
        return get(getConvert(id));
    }

    public MessageStore getFromOwnId(long id) {
        return get(id);
    }
}
