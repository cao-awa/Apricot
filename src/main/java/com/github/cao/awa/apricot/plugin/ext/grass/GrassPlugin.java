package com.github.cao.awa.apricot.plugin.ext.grass;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.plugin.ext.grass.handler.mute.*;
import com.github.cao.awa.apricot.plugin.ext.grass.handler.response.*;
import com.github.cao.awa.apricot.plugin.name.*;
import org.jetbrains.annotations.*;

/**
 * The "草二号机" plugin for chatting.
 *
 * @author cao_awa
 * @author 草二号机
 */
@AutoPlugin
public class GrassPlugin extends Plugin {
    @Override
    public @NotNull PluginName name() {
        return PluginName.of(
                "Grass",
                "草二号机"
        );
    }

    @Override
    public void onInitialize() {
        registerHandler(new QuickResponse());
        registerHandler(new MuteMe());
    }

    @Override
    public String version() {
        return "1.0.0";
    }
}
