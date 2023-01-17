package com.github.cao.awa.apricot.event.handler.invalid;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.invalid.*;

public abstract class InvalidDataReceivedEventHandler extends EventHandler<InvalidDataReceivedEvent> {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public final String getType() {
        return "invalid-data";
    }

    /**
     * Process event.
     *
     * @param event
     *         event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    public abstract void onInvalid(InvalidDataReceivedEvent event);
}
