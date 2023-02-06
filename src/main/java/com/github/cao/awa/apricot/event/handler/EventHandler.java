package com.github.cao.awa.apricot.event.handler;

import com.github.cao.awa.apricot.config.plugin.handler.*;
import com.github.cao.awa.apricot.event.receive.*;
import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.task.intensive.*;

public abstract class EventHandler<T extends Event<?>> {
    private Plugin plugin;

    public Plugin getPlugin() {
        return this.plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    public abstract String getType();

    public boolean accept(EventTarget target) {
        return getPlugin().isAllow(target);
    }

    public ApsConfig config() {
        return getPlugin().config();
    }

    public ApsConfig config(String name) {
        return getPlugin().config(name);
    }

    public void onExclusive(T event) {

    }

    public void onException(Exception exception) {

    }

    public boolean compulsory() {
        return this instanceof Compulsory;
    }

    public IntensiveType intensive() {
        return IntensiveType.CPU;
    }

    public void reload() {

    }
}
