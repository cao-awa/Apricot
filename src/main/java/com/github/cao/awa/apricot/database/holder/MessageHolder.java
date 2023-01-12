package com.github.cao.awa.apricot.database.holder;

import com.github.cao.awa.apricot.database.message.store.*;
import com.github.cao.awa.apricot.database.simple.*;

public class MessageHolder {
    private final MessageDatabase database;
    private final SerialLongKvDatabase rel;

    public MessageHolder(MessageDatabase database, SerialLongKvDatabase rel) {
        this.database = database;
        this.rel = rel;
    }
}
