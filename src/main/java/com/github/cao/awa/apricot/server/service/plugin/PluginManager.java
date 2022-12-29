package com.github.cao.awa.apricot.server.service.plugin;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.plugin.accomplish.*;
import com.github.cao.awa.apricot.plugin.firewall.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.server.service.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import org.apache.logging.log4j.*;

import java.util.*;
import java.util.concurrent.*;

import static com.github.cao.awa.apricot.server.ApricotServer.CLAZZ_SCANNER;

public class PluginManager implements ConcurrentService {
    private static final Logger LOGGER = LogManager.getLogger("PluginManager");
    private final ApricotServer server;
    private final Map<UUID, AccomplishPlugin> accomplishPlugins = new ConcurrentHashMap<>();
    private final Map<UUID, FirewallPlugin> firewallPlugins = new ConcurrentHashMap<>();
    private final Executor executor;
    private boolean active = true;

    public PluginManager(ApricotServer server, Executor executor) {
        this.server = server;
        this.executor = executor;
    }

    public ApricotServer getServer() {
        return this.server;
    }

    public Collection<AccomplishPlugin> getAccomplishPlugins() {
        return this.accomplishPlugins.values();
    }

    public Collection<FirewallPlugin> getFirewallPlugins() {
        return this.firewallPlugins.values();
    }

    public AccomplishPlugin getAccomplishPlugin(UUID uuid) {
        return this.accomplishPlugins.get(uuid);
    }

    public FirewallPlugin getFirewallPlugin(UUID uuid) {
        return this.firewallPlugins.get(uuid);
    }

    @Override
    public void shutdown() {
        this.active = false;
        if (this.executor instanceof ThreadPoolExecutor threadPool) {
            threadPool.shutdown();
        }
    }

    public void loadPlugins() {
        if (this.active) {
            LOGGER.info("Loading apricot bot plugins");

            List<Class<?>> classList = CLAZZ_SCANNER.getTypesAnnotatedWith(AutoPlugin.class);

            boolean shouldAsync = server.shouldAsyncLoadPlugins();

            List<Plugin> blockLoading = ApricotCollectionFactor.newArrayList();

            for (Class<?> clazz : classList) {
                EntrustEnvironment.trys(
                        () -> {
                            Plugin plugin = (Plugin) clazz.getDeclaredConstructor()
                                                          .newInstance();
                            if (shouldAsync && plugin.shouldAsync()) {
                                this.executor.execute(() -> loadPlugin(plugin));
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
            plugin.setServer(this.server);
            plugin.onInitialize();
            if (plugin instanceof FirewallPlugin firewall) {
                registerFirewall(firewall);
            } else if (plugin instanceof AccomplishPlugin accomplish) {
                registerAccomplish(accomplish);
            }
            LOGGER.info(
                    "Plugins '{}'({}) registered",
                    plugin.getName(),
                    plugin.getUuid()
            );
        }
    }

    public void registerAccomplish(AccomplishPlugin plugin) {
        this.accomplishPlugins.put(
                plugin.getUuid(),
                plugin
        );
    }

    public void registerFirewall(FirewallPlugin plugin) {
        this.firewallPlugins.put(
                plugin.getUuid(),
                plugin
        );
    }
}
