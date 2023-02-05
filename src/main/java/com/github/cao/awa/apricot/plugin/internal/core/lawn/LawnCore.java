package com.github.cao.awa.apricot.plugin.internal.core.lawn;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.plugin.internal.core.lawn.handler.echo.*;
import com.github.cao.awa.apricot.plugin.internal.core.lawn.handler.message.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * An example plugin.
 *
 * @author cao_awa
 * @since 1.0.0
 */
public class LawnCore extends Plugin implements Compulsory {
    private static final UUID ID = UUID.fromString("8e599f58-703f-4dc9-beb8-abee377fb39a");

    @Override
    public UUID getUuid() {
        return ID;
    }

    @Override
    public @NotNull String getName() {
        return "生草机核心";
    }

    @Override
    public void onInitialize() {
        // Do not delete these handlers.
        registerHandlers(
                new OthersMessageStorage(),
                new SelfMessageStore(),
                new RecalledMessageHandler(),
                new MessageExport()
        );
        registerHandlers(new InternalEchoResultHandler());
    }

    /**
     * The core plugin need ordered to pipeline, parallel may cause information be incorrect.
     *
     * @return parallel
     */
    @Override
    public boolean parallel() {
        return false;
    }

    public boolean isCore() {
        return true;
    }

    @Override
    public String version() {
        return "1.0.5";
    }
}
