package com.github.cao.awa.apricot.plugin.internal.lawn;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.plugin.internal.lawn.handler.*;
import com.github.cao.awa.apricot.plugin.internal.lawn.handler.message.recall.reproduce.*;
import org.jetbrains.annotations.*;

@AutoPlugin
public class Lawn extends Plugin {
    @Override
    public @NotNull String getName() {
        return "生草机";
    }

    @Override
    public void onInitialize() {
        // Features handlers.
        registerHandler(new Tests());
        registerHandler(new ApricotInformation());
        registerHandler(new PokeReciprocity());
        //        registerHandler(new RecallNotice());
        registerHandler(new MessageReproducer());
    }

    @Override
    public String version() {
        return "0.0.2";
    }
}
