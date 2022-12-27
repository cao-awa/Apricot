package com.github.cao.awa.bot.plugin.internal;

import com.github.cao.awa.bot.plugin.*;

import java.nio.charset.*;
import java.util.*;

public class InternalPlugin extends Plugin {
    public InternalPlugin() {
        registerHandler(new InternalMessageReceivedHandler());
    }

    @Override
    public UUID getUuid() {
        return UUID.nameUUIDFromBytes("az".getBytes(StandardCharsets.UTF_8));
    }
}
