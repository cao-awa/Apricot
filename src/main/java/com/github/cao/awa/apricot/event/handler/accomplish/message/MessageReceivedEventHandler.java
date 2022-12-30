package com.github.cao.awa.apricot.event.handler.accomplish.message;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.personal.*;

public abstract class MessageReceivedEventHandler extends AccomplishEventHandler {
    @Override
    public final String getType() {
        return "message-received";
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
    public abstract void onMessageReceived(MessageReceivedEvent<?> event);
}
