package com.github.cao.awa.apricot.plugin.ext.hasnasu;

import com.github.cao.awa.apricot.anntations.AutoPlugin;
import com.github.cao.awa.apricot.plugin.Plugin;
import com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.HanasuHandler;
import com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.learn.HanasuLearn;
import com.github.cao.awa.apricot.plugin.name.PluginName;
import org.jetbrains.annotations.NotNull;

@AutoPlugin
public class HanasuPlugin extends Plugin {
    @Override
    public @NotNull PluginName name() {
        return PluginName.of("Hanasu", "はなすAI");
    }

    @Override
    public void initialize() {
        registerHandlers(new HanasuHandler(), new HanasuLearn());
    }
}
