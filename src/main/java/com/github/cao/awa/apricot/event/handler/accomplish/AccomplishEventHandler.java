package com.github.cao.awa.apricot.event.handler.accomplish;

import com.github.cao.awa.apricot.target.*;

public abstract class AccomplishEventHandler {
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
