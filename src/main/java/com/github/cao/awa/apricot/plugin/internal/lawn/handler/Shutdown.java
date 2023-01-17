package com.github.cao.awa.apricot.plugin.internal.lawn.handler;

import com.github.cao.awa.apricot.event.handler.message.received.*;
import com.github.cao.awa.apricot.event.receive.message.*;

public class Shutdown extends MessageReceivedEventHandler {
    /**
     * Process event.
     *
     * @param event
     *         event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent<?> event) {
        if (event.getPacket().getMessage().toPlainText().equals(".shutdown")) {
            event.getProxy().server().shutdown();
        }
    }
}
