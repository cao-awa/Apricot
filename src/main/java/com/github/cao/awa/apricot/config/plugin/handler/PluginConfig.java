package com.github.cao.awa.apricot.config.plugin.handler;

import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.util.collection.*;

import java.util.*;

public class PluginConfig {
    private final String configPath;
    private final String defaultConfigPath;
    private final Map<String, ApsConfig> configs = ApricotCollectionFactor.concurrentHashMap();
    private final ApsConfig pluginConfig;

    public PluginConfig(Plugin plugin, String prefix) {
        this.configPath = "configs/plugins/" + prefix + "/" + plugin.name()
                                                                    .eName();
        this.defaultConfigPath = "assets/plugins/" + plugin.name()
                                                           .eName();
        this.pluginConfig = new ApsConfig(
                this.configPath + "/" + plugin.name()
                                              .eName() + ".json",
                this.defaultConfigPath + "/" + plugin.name()
                                                     .eName() + ".json"
        );
    }

    public ApsConfig config() {
        return this.pluginConfig;
    }

    public ApsConfig config(String name) {
        return this.configs.compute(
                name,
                (key, config) -> config == null ?
                                 new ApsConfig(
                                         this.configPath + "/" + name + "/" + name + ".json",
                                         this.defaultConfigPath + "/" + name + "/" + name + ".json"
                                 ) :
                                 config
        );
    }

    public ApsConfig reload() {
        return this.pluginConfig.reload();
    }

    public ApsConfig write() {
        return this.pluginConfig.write();
    }

    public PluginConfig reloadAll() {
        reload();
        this.configs.values()
                    .forEach(ApsConfig::reload);
        return this;
    }

    public PluginConfig writeAll() {
        write();
        this.configs.values()
                    .forEach(ApsConfig::write);
        return this;
    }
}
