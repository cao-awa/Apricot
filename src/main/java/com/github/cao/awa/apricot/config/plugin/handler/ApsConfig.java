package com.github.cao.awa.apricot.config.plugin.handler;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.config.util.*;
import com.github.cao.awa.apricot.resource.loader.*;
import com.github.cao.awa.apricot.util.io.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.*;

public class ApsConfig {
    private final File configFile;
    private final Function<JSONObject, JSONObject> configLoader;
    private final JSONObject configure = new JSONObject();

    public ApsConfig(@NotNull String config, @NotNull String defaultConfig) {
        this.configFile = new File(config);
        this.configLoader = json -> ConfigUtil.jsonFile(
                json,
                config,
                () -> IOUtil.read(new InputStreamReader(ResourcesLoader.get(defaultConfig), StandardCharsets.UTF_8))
        );
        reload();
    }

    @NotNull
    public ApsConfig reload() {
        this.configLoader.apply(this.configure);
        return this;
    }

    @NotNull
    public ApsConfig write() {
        EntrustEnvironment.trys(() -> IOUtil.write(
                new FileWriter(configFile),
                this.configure.toString()
        ));
        return this;
    }

    @NotNull
    public JSONArray array(@NotNull String key) {
        JSONArray array = this.configure.getJSONArray(key);
        if (array == null) {
            array = new JSONArray();
            put(
                    key,
                    array
            );
        }
        return array;
    }

    @NotNull
    public <T> List<T> array(@NotNull String key, Class<T> type) {
        JSONArray array = this.configure.getJSONArray(key);
        if (array == null) {
            array = new JSONArray();
            put(
                    key,
                    array
            );
        }
        return array.toList(type);
    }

    @NotNull
    public String str(@NotNull String key) {
        return get(
                key,
                () -> ""
        );
    }

    @NotNull
    public <T> ApsConfig put(@NotNull String key, T target) {
        this.configure.put(
                key,
                target
        );
        write();
        return this;
    }

    public boolean bool(@NotNull String key) {
        return get(
                key,
                () -> false
        );
    }

    public float floatPoint(@NotNull String key) {
        return get(
                key,
                () -> 0F
        );
    }

    public double doubleFloat(@NotNull String key) {
        return get(
                key,
                () -> 0D
        );
    }

    @NotNull
    public JSONObject map(@NotNull String key) {
        JSONObject map = this.configure.getJSONObject(key);
        if (map == null) {
            map = new JSONObject();
            put(
                    key,
                    map
            );
        }
        return map;
    }

    public <T> T get(@NotNull String key, @NotNull Supplier<T> creator) {
        T target = get(key);
        if (target == null) {
            target = creator.get();
            put(
                    key,
                    target
            );
        }
        return target;
    }

    public boolean is(@NotNull String key) {
        return bool(key);
    }

    public <T> boolean is(@NotNull String key, T target) {
        T element = get(key);
        return element == null ?
               target == null :
               element.getClass()
                      .isInstance(target) && element.equals(target);
    }

    public <T> T get(@NotNull String key) {
        return EntrustEnvironment.cast(this.configure.get(key));
    }

    public int integer(@NotNull String key) {
        return get(
                key,
                () -> 0
        );
    }

    public long longInteger(@NotNull String key) {
        return get(
                key,
                () -> 0L
        );
    }

    @NotNull
    public <T> ApsConfig putIfAbsent(@NotNull String key, Supplier<T> creator) {
        if (! this.configure.containsKey(key)) {
            this.configure.put(
                    key,
                    creator.get()
            );
        }
        return this;
    }

    @NotNull
    public <T> ApsConfig putIfAbsent(@NotNull String key, T creator) {
        if (! this.configure.containsKey(key)) {
            this.configure.put(
                    key,
                    creator
            );
        }
        return this;
    }

    @NotNull
    public <T> ApsConfig putIfPresent(@NotNull String key, Supplier<T> creator) {
        if (this.configure.containsKey(key)) {
            this.configure.put(
                    key,
                    creator.get()
            );
        }
        return this;
    }

    @NotNull
    public <T> ApsConfig putIfPresent(@NotNull String key, T creator) {
        if (this.configure.containsKey(key)) {
            this.configure.put(
                    key,
                    creator
            );
        }
        return this;
    }
}
