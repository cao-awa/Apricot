package com.github.cao.awa.apricot.plugin.ext.lawn;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.plugin.ext.lawn.handler.*;
import com.github.cao.awa.apricot.plugin.ext.lawn.handler.media.MediaDownload;
import com.github.cao.awa.apricot.plugin.ext.lawn.handler.message.export.MessageExport;
import com.github.cao.awa.apricot.plugin.ext.lawn.handler.message.recall.reproduce.*;
import com.github.cao.awa.apricot.plugin.ext.lawn.handler.reload.*;
import com.github.cao.awa.apricot.plugin.name.*;
import org.jetbrains.annotations.*;

@AutoPlugin
public class Lawn extends Plugin {
    @Override
    public @NotNull PluginName name() {
        return PluginName.of(
                "Lawn",
                "生草机"
        );
    }

    @Override
    public void initialize() {
        // Features handlers.
//        registerHandler(new Tests());
        registerHandler(new ApricotInformation());
        registerHandler(new PokeReciprocity());
        registerHandler(new MessageExport());
        //        registerHandler(new RecallNotice());
        registerHandler(new MessageReproducer());
        registerHandler(new Reloading());
        registerHandler(new MediaDownload());
    }

    @Override
    public String version() {
        return "1.0.2";
    }
}
