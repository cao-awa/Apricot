package com.github.cao.awa.apricot.config.plugin.handler;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.config.util.*;
import com.github.cao.awa.apricot.resource.loader.*;
import com.github.cao.awa.apricot.util.io.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.io.*;
import java.util.function.*;

public class ApsConfig {
    private final File configFile;
    private final Function<JSONObject, JSONObject> configLoader;
    private final JSONObject configure = new JSONObject();

    public ApsConfig(String config, String defaultConfig) {
        this.configFile = new File(config);
        this.configLoader = json -> ConfigUtil.jsonFile(
                json,
                config,
                () -> IOUtil.read(ResourcesLoader.get(defaultConfig))
        );
        reload();
    }

    public ApsConfig reload() {
        this.configLoader.apply(this.configure);
        return this;
    }

    public ApsConfig write() {
        EntrustEnvironment.trys(() -> IOUtil.write(
                new FileWriter(configFile),
                this.configure.toString()
        ));
        return this;
    }

    public JSONArray array(String key) {
        JSONArray array = this.configure.getJSONArray(key);
        if (array == null) {
            array = new JSONArray();
            this.configure.put(
                    key,
                    array
            );
        }
        return array;
    }

    public boolean bool(String key) {
        return this.configure.containsKey(key) && this.configure.getBoolean(key);
    }

    public boolean is(String key) {
        return this.configure.containsKey(key) && this.configure.getBoolean(key);
    }

    public int integer(String key) {
        return this.configure.containsKey(key) ? this.configure.getInteger(key) : 0;
    }

    public long longInteger(String key) {
        return this.configure.containsKey(key) ? this.configure.getLong(key) : 0;
    }

    public <T> ApsConfig putIfAbsent(String key, Supplier<T> creator) {
        if (! this.configure.containsKey(key)) {
            this.configure.put(
                    key,
                    creator.get()
            );
        }
        return this;
    }

    public <T> ApsConfig putIfAbsent(String key, T creator) {
        if (! this.configure.containsKey(key)) {
            this.configure.put(
                    key,
                    creator
            );
        }
        return this;
    }

    public <T> ApsConfig putIfPresent(String key, Supplier<T> creator) {
        if (this.configure.containsKey(key)) {
            this.configure.put(
                    key,
                    creator.get()
            );
        }
        return this;
    }

    public <T> ApsConfig putIfPresent(String key, T creator) {
        if (this.configure.containsKey(key)) {
            this.configure.put(
                    key,
                    creator
            );
        }
        return this;
    }
}
