package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.plugin.accomplish.*;
import com.github.cao.awa.apricot.plugin.internal.plugin.message.*;
import org.jetbrains.annotations.*;

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
//        registerHandler(new AboutApricot());
//        registerHandler(new ApricotInformation());
//        registerHandler(new ListenMute());
//        registerHandler(new ListenLiftMute());
//        registerHandler(new ListenAddGroup());
//        registerHandler(new RecallNotice());
        registerHandler(new InternalEchoResultHandler());
        registerHandler(new PokeReciprocity());
//        registerHandler(new Jrrp());
        registerHandler(new MessageStorage());
        registerHandler(new MessageExport());
        registerHandler(new MessageReproduce());
    }

    @Override
    public String version() {
        return "0.0.3";
    }

    @Override
    public UUID getUuid() {
        return ID;
    }

    @Override
    public @NotNull String getName() {
        return "生草机";
    }
}
