package com.github.cao.awa.apricot.plugin;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.*;
import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.task.intensive.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import org.jetbrains.annotations.*;

import java.nio.charset.*;
import java.util.*;

/**
 * Plugins for handle bot events.
 *
 * @author cao_awa
 * @author 草二号机
 * @since 1.0.0
 */
public abstract class Plugin implements Comparable<Plugin> {
    private final Map<String, List<EventHandler<?>>> handlers = ApricotCollectionFactor.newHashMap();
    private ApricotServer server;

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

    public UUID getUuid() {
        return UUID.nameUUIDFromBytes(getName().getBytes(StandardCharsets.UTF_8));
    }

    @NotNull
    public abstract String getName();

    public abstract void onInitialize();

    public boolean canAsync() {
        return true;
    }

    public abstract String version();

    public final void registerHandlers(EventHandler<?> handler, EventHandler<?>... handlers) {
        registerHandler(handler);
        for (EventHandler<?> eventHandler : handlers) {
            registerHandler(eventHandler);
        }
    }

    public final void registerHandler(EventHandler<?> handler) {
        if (! this.handlers.containsKey(handler.getType())) {
            this.handlers.put(
                    handler.getType(),
                    ApricotCollectionFactor.newLinkedList()
            );
        }
        this.handlers.get(handler.getType())
                     .add(handler);
        handler.setPlugin(this);
        handler.reload();
    }

    /**
     * Let an event be fired.
     *
     * @param event
     *         event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    public final void fireEvent(Event<?> event) {
        this.fireEvent(
                event,
                null,
                true
        );
    }

    /**
     * Let an event be fired.
     *
     * @param event
     *         event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    private void fireEvent(Event<?> event, EventHandler<?> exclude, boolean compulsory) {
        EventTarget target = event.getPacket()
                                  .target();
        event.pipeline()
             .forEach(type -> EntrustEnvironment.notNull(
                     this.handlers.get(type),
                     handlers -> EntrustEnvironment.operation(
                             exclude == null || compulsory ?
                             handlers.stream() :
                             handlers.stream()
                                     .filter(eventHandler -> eventHandler.compulsory() && eventHandler != exclude),
                             valid -> valid.filter(handler -> handler.accept(target))
                                           .forEach(handler -> {
                                               if (parallel()) {
                                                   EntrustEnvironment.operation(
                                                           handler.intensive() == IntensiveType.CPU ?
                                                           getServer().intensiveCpu() :
                                                           getServer().intensiveIo(),
                                                           manager -> manager.execute(
                                                                   getName(),
                                                                   () -> fireEvent(
                                                                           handler,
                                                                           event
                                                                   )
                                                           )
                                                   );
                                               } else {
                                                   fireEvent(
                                                           handler,
                                                           event
                                                   );
                                               }
                                           })

                     )
             ));
    }

    private static void fireEvent(EventHandler<?> handler, Event<?> event) {
        EntrustEnvironment.trys(
                () -> event.fireEvent(handler),
                handler::onException
        );
    }

    public final ApricotServer getServer() {
        return this.server;
    }

    public final void setServer(ApricotServer server) {
        this.server = server;
    }

    public boolean parallel() {
        return true;
    }

    /**
     * Let an event be fired.
     *
     * @param event
     *         event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    public void fireEvent(Event<?> event, EventHandler<?> exclude) {
        this.fireEvent(
                event,
                exclude,
                this.compulsory()
        );
    }

    public boolean compulsory() {
        return this instanceof Compulsory;
    }

    public void reload() {
        this.handlers.values()
                     .forEach(handlers -> handlers.forEach(EventHandler::reload));
    }

    public boolean isCore() {
        return false;
    }
}
