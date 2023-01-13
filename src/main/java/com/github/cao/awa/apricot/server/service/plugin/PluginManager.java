package com.github.cao.awa.apricot.server.service.plugin;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.resource.loader.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.server.service.*;
import com.github.cao.awa.apricot.server.service.plugin.loader.*;
import com.github.cao.awa.apricot.thread.pool.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import org.apache.logging.log4j.*;
import org.reflections.*;
import org.reflections.scanners.*;
import org.reflections.util.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;

public class PluginManager implements ConcurrentService {
    private static final Logger LOGGER = LogManager.getLogger("PluginManager");
    private final ApricotServer server;
    private final Map<UUID, Plugin> plugins = new ConcurrentHashMap<>();
    private final ExecutorEntrust executor;
    private boolean active = true;

    public PluginManager(ApricotServer server, Executor executor) {
        this.server = server;
        this.executor = new ExecutorEntrust(executor);
    }

    public ApricotServer getServer() {
        return this.server;
    }

    public Collection<Plugin> getPlugins() {
        return this.plugins.values();
    }

    public boolean isActive() {
        return this.active;
    }

    public Plugin getPlugin(UUID uuid) {
        return this.plugins.get(uuid);
    }

    @Override
    public void shutdown() {
        this.active = false;
        if (this.executor.executor() instanceof ThreadPoolExecutor threadPool) {
            threadPool.shutdown();
        }
    }

    public void loadPlugins() {
        if (this.active) {
            LOGGER.info("Loading external plugins");

            Set<Class<?>> plugins = Objects.requireNonNull(EntrustEnvironment.trys(() -> {
                File apricotJar = new File(URLDecoder.decode(
                        ResourcesLoader.class.getProtectionDomain()
                                             .getCodeSource()
                                             .getLocation()
                                             .getPath(),
                        StandardCharsets.UTF_8
                ));

                Set<Class<?>> results = ApricotCollectionFactor.newHashSet();

                File pluginDir = new File("plugins");
                AnnotatedClassFinder finder = new AnnotatedClassFinder(
                        pluginDir,
                        AutoPlugin.class
                );
                results.addAll(finder.getClasses()
                                     .values());

                results.addAll(apricotJar.isFile() ?
                               new Reflections(new ConfigurationBuilder().addUrls(apricotJar.toURI()
                                                                                            .toURL())
                                                                         .addScanners(Scanners.TypesAnnotated)).getTypesAnnotatedWith(AutoPlugin.class) :
                               new Reflections(new ConfigurationBuilder().addUrls(ClasspathHelper.forPackage(""))
                                                                         .addScanners(Scanners.TypesAnnotated)).getTypesAnnotatedWith(AutoPlugin.class));
                return results;
            }));

            boolean shouldAsync = server.shouldAsyncLoadPlugins();

            List<Plugin> blockLoading = ApricotCollectionFactor.newArrayList();

            for (Class<?> clazz : plugins) {
                EntrustEnvironment.trys(
                        () -> {
                            Plugin plugin = (Plugin) clazz.getDeclaredConstructor()
                                                          .newInstance();
                            if (shouldAsync && plugin.canAsync()) {
                                this.executor.execute(
                                        "PluginManager",
                                        () -> loadPlugin(plugin)
                                );
                            } else {
                                blockLoading.add(plugin);
                            }
                        },
                        ex -> {
                            LOGGER.info(
                                    "Failed to register auto plugin: {}",
                                    clazz.getName(),
                                    ex
                            );
                        }
                );
            }

            blockLoading.forEach(this::loadPlugin);
        }
    }

    public void loadPlugin(Plugin plugin) {
        if (this.active) {
            register(plugin);
        }
    }

    public void register(Plugin plugin) {
        plugin.setServer(this.server);
        plugin.onInitialize();
        this.plugins.put(
                plugin.getUuid(),
                plugin
        );
        LOGGER.info(
                "Plugins '{}'({}) registered",
                plugin.getName(),
                plugin.getUuid()
        );
    }
}
