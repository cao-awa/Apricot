package com.github.cao.awa.apricot.event.handler.accomplish.message;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;

public abstract class MessageEventHandler extends AccomplishEventHandler {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public String getType() {
        return "message";
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
    public abstract void onMessage(MessageEvent<?> event);
}
