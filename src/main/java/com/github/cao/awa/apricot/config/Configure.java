package com.github.cao.awa.apricot.config;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

/**
 * Configuration readonly, changes will temporally apply.
 * Unable rewrite to file.
 *
 * @author cao_awa
 */
@Stable
public class Configure {
    private static final @NotNull Logger LOGGER = LogManager.getLogger("Configure");
    private final @NotNull Map<String, Map<String, String>> warning = ApricotCollectionFactor.hashMap();
    private final @NotNull Map<String, String> configs = ApricotCollectionFactor.hashMap();
    private @NotNull Supplier<String> loader;
    private final Map<String, String> defaultValues = ApricotCollectionFactor.hashMap();

    /**
     * Setting basic prepares.
     *
     * @param loader
     *         Support loading config information
     */
    public Configure(@NotNull Supplier<String> loader) {
        this.loader = loader;
    }

    /**
     * Reload configurations.
     */
    public void reload() {
        load();
    }

    /**
     * Read and load configs.
     */
    public void load() {
        load(this.loader.get());
    }

    /**
     * Load config from cold data.
     *
     * @param configInformation
     *         Config deltas
     */
    private void load(@NotNull String configInformation) {
        final BufferedReader reader = new BufferedReader(new StringReader(configInformation));
        EntrustEnvironment.trys(() -> {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    line = line.trim()
                               .strip();
                    if (line.startsWith("#")) {
                        // Note here
                        LOGGER.debug("Configure note: " + line);
                    } else {
                        int note = line.indexOf("#");
                        if (note != - 1) {
                            line = line.substring(
                                    0,
                                    note
                            );
                            LOGGER.warn("The '#' should not be appeared in properties line, ignored it");
                        }
                        int delimiter = line.indexOf("=");
                        if (delimiter == - 1) {
                            continue;
                        }
                        String key = line.substring(
                                                 0,
                                                 delimiter
                                         )
                                         .strip()
                                         .trim();
                        String value = line.substring(delimiter + 1)
                                           .strip()
                                           .trim();

                        EntrustEnvironment.equals(
                                "",
                                key,
                                () -> EntrustEnvironment.equals(
                                        "",
                                        value,
                                        () -> LOGGER.warn("The key and value not found in configuration line"),
                                        () -> LOGGER.warn("The key not found in configuration line")
                                ),
                                value,
                                () -> LOGGER.info(
                                        "The value not found in configuration line for '{}'",
                                        key
                                ),
                                () -> {
                                    LOGGER.info(
                                            "Processing config: '{}' = '{}'",
                                            key,
                                            value
                                    );

                                    set(
                                            key,
                                            value
                                    );

                                    EntrustEnvironment.notNull(
                                            this.warning.get(key),
                                            warnings -> EntrustEnvironment.notNull(
                                                    warnings.get(value),
                                                    LOGGER::warn
                                            )
                                    );
                                }
                        );
                    }
                } catch (Exception e) {
                    LOGGER.warn(
                            "Unable to load the the configuration line: '{}'",
                            line,
                            e
                    );
                }
            }
        });
    }

    /**
     * Set config.
     *
     * @param key
     *         Config key
     * @param value
     *         Config value
     */
    public void set(@NotNull String key, @NotNull String value) {
        this.configs.put(
                key,
                value
        );
    }

    /**
     * Set config loader and load.
     *
     * @param loader
     *         Information loader
     */
    public void init(@NotNull Supplier<String> loader) {
        setLoader(loader);
        load();
    }

    /**
     * Set config loader.
     *
     * @param loader
     *         Information loader
     */
    public void setLoader(@NotNull Supplier<String> loader) {
        this.loader = loader;
    }

    /**
     * Get config.
     *
     * @param key
     *         Config key
     * @return Config value
     */
    @NotNull
    public String get(String key) {
        return this.configs.getOrDefault(
                key,
                ""
        );
    }

    /**
     * Prepare warning for readied value match to expect values.
     *
     * @param key
     *         Config key
     * @param values
     *         Config values
     * @param info
     *         Warning when config match to target
     */
    public void warningWhen(String key, List<String> values, String info) {
        values.forEach(value -> warningWhen(
                key,
                value,
                info
        ));
    }

    /**
     * Prepare warning for readied value match to expect value.
     *
     * @param key
     *         Config key
     * @param value
     *         Config value
     * @param info
     *         Warning when config match to target
     */
    public void warningWhen(String key, String value, String info) {
        Map<String, String> map = this.warning.computeIfAbsent(
                key,
                k -> ApricotCollectionFactor.hashMap()
        );
        map.put(
                value,
                info
        );
    }

    public boolean getBoolean(String key) {
        return getOrDefault(key).equals("true");
    }

    public int getInteger(String key) {
        return Integer.parseInt(getOrDefault(key));
    }

    public Configure setDefault(@NotNull String key, @NotNull Object value) {
        this.defaultValues.put(key, Objects.toString(value));
        return this;
    }

    public String getOrDefault(String key) {
        String defaultValue = this.defaultValues.get(key);
        String current = get(key);
        if (current.equals("") && defaultValue != null) {
            return defaultValue;
        }
        return current;
    }
}
