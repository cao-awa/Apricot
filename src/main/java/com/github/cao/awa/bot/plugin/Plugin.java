package com.github.cao.awa.bot.plugin;

import com.github.cao.awa.bot.event.*;
import com.github.cao.awa.bot.event.handler.*;
import com.github.cao.awa.bot.event.pipeline.*;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class Plugin implements Comparable<Plugin> {
    private final Map<String, List<EventHandler>> handlers = new Object2ObjectOpenHashMap<>();

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param o
     *         the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     *
     * @throws NullPointerException
     *         if the specified object is null
     * @throws ClassCastException
     *         if the specified object's type prevents it
     *         from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     */
    @Override
    public int compareTo(@NotNull Plugin o) {
        return getUuid().compareTo(o.getUuid());
    }

    public abstract UUID getUuid();

    public void registerHandler(EventHandler handler) {
        if (!this.handlers.containsKey(handler.getName())) {
            this.handlers.put(handler.getName(), new LinkedList<>());
        }
        this.handlers.get(handler.getName()).add(handler);
    }

    public void registerHandlers(EventHandler handler, EventHandler... handlers) {
        registerHandler(handler);
        for (EventHandler eventHandler : handlers) {
            registerHandler(eventHandler);
        }
    }

    public void fireEvent(Event event) {
        List<EventHandler> handlers = this.handlers.get(event.getName());
        if (handlers != null) {
            handlers.forEach(event::entrust);
        }
    }
}
