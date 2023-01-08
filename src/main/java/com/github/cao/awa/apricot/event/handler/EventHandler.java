package com.github.cao.awa.apricot.event.handler;

import com.github.cao.awa.apricot.target.*;

public abstract class EventHandler {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    public abstract String getType();

    public boolean accept(EventTarget target) {
        return true;
    }
}
