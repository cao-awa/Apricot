package com.github.cao.awa.apricot.event.handler.message;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.message.*;

public abstract class MessageReceivedEventHandler extends EventHandler {
    @Override
    public final String getName() {
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
    public abstract void onMessageReceived(MessageReceivedEvent event);
}
