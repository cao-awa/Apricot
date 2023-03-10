package com.github.cao.awa.apricot.event.handler.message.sent.personal;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.message.personal.sent.*;

public abstract class PrivateMessageSentEventHandler extends EventHandler<PrivateMessageSentEvent<?>> {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public final String getType() {
        return "message-private";
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
    public abstract void onMessageSent(PrivateMessageSentEvent<?> event);
}
