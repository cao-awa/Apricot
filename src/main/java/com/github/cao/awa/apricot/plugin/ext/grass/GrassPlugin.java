package com.github.cao.awa.apricot.plugin.ext.grass;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.plugin.ext.grass.handler.delete.*;
import com.github.cao.awa.apricot.plugin.ext.grass.handler.hanasu.Hanasu;
import com.github.cao.awa.apricot.plugin.ext.grass.handler.hanasu.learn.HanasuLearn;
import com.github.cao.awa.apricot.plugin.ext.grass.handler.hitokoto.*;
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
    public void initialize() {
        registerHandler(new QuickResponse());
        registerHandler(new MuteMe());
        registerHandler(new Hitokoto());
        registerHandler(new FastDelete());
        registerHandlers(new Hanasu(), new HanasuLearn());
    }

    @Override
    public String version() {
        return "1.0.0";
    }
}
