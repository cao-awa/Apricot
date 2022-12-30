package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.plugin.accomplish.*;

import java.util.*;

/**
 * An example plugin.
 *
 * @since 1.0.0
 * @author cao_awa
 */
@AutoPlugin
public class InternalPlugin extends AccomplishPlugin {
    private static final UUID ID = UUID.fromString("8e599f58-703f-4dc9-beb8-abee377fb39a");

    @Override
    public void onInitialize() {
        registerHandler(new InternalGroupHandler());
//        registerHandler(new InternalPrivateHandler());
        registerHandler(new ListenBotDied());
        registerHandler(new RecallNotice());
        registerHandler(new InternalEchoResultHandler());
        registerHandler(new InternalIllegalImmigrationHandler());
        registerHandler(new PokeReciprocity());
    }

    @Override
    public UUID getUuid() {
        return ID;
    }

    @Override
    public String getName() {
        return "Internal Plugin";
    }
}
