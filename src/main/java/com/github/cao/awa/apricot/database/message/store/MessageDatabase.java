package com.github.cao.awa.apricot.database.message.store;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.database.*;
import com.github.cao.awa.apricot.math.base.*;
import com.github.cao.awa.apricot.message.store.*;
import com.github.cao.awa.apricot.server.*;
import org.iq80.leveldb.*;

import java.nio.charset.*;
import java.util.function.*;

public class MessageDatabase extends ApricotDatabase<Long, MessageStore> {
    private final ApricotServer server;
    private final DB head;
    private final DB convert;

    public MessageDatabase(ApricotServer server, DB head, DB convert) {
        this.server = server;
        this.head = head;
        this.convert = convert;
    }

    @Override
    public void forEach(BiConsumer<Long, MessageStore> action) {
        this.head.forEach(entry -> {
            action.accept(
                    Base256.longFromBuf(entry.getKey()),
                    MessageStore.fromJSONObject(
                            this.server,
                            JSONObject.parse(new String(
                                    entry.getValue(),
                                    StandardCharsets.UTF_8
                            ))
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
                Base256.longToBuf(value.getMessageId()),
                Base256.longToBuf(key)
        );
    }

    @Override
    public MessageStore get(Long key) {
        return MessageStore.fromJSONObject(
                this.server,
                JSONObject.parse(new String(
                        this.head.get(Base256.longToBuf(key)),
                        StandardCharsets.UTF_8
                ))
        );
    }

    @Override
    public MessageStore delete(Long key) {
        MessageStore result = get(key);
        this.head.delete(Base256.longToBuf(key));
        return result;
    }

    public void setFromId(long id, MessageStore value) {
        set(
                getConvert(id),
                value
        );
    }

    public long getConvert(long id) {
        return Base256.longFromBuf(this.convert.get(Base256.longToBuf(id)));
    }

    public MessageStore getFromId(long id) {
        return get(getConvert(id));
    }

    public MessageStore getFromOwnId(long id) {
        return get(id);
    }
}
