package com.github.cao.awa.apricot.plugin.internal;

import com.github.cao.awa.apricot.plugin.*;

import java.util.*;

/**
 * An example plugin.
 *
 * @since 1.0.0
 * @author cao_awa
 */
public class InternalPlugin extends Plugin {
    private static final UUID ID = UUID.fromString("8e599f58-703f-4dc9-beb8-abee377fb39a");

    public InternalPlugin() {
        registerHandler(new InternalMessageReceivedHandler());
//        registerHandler(new InternalMessageReceivedHandler2());
    }

    @Override
    public UUID getUuid() {
        return ID;
    }
}
