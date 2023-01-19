package com.github.cao.awa.apricot.plugin.internal.example.exclusive;

import com.github.cao.awa.apricot.event.handler.message.received.group.*;
import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import com.github.cao.awa.apricot.target.*;

public class NormalHandler2 extends GroupMessageReceivedEventHandler {
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
    public void onMessageReceived(GroupMessageReceivedEvent<?> event) {
        System.out.println("H2 a.");
    }

    @Override
    public void onExclusive(GroupMessageReceivedEvent<?> event) {
        System.out.println("H2 e.");
    }

    @Override
    public boolean accept(EventTarget target) {
        return target.group() == 252755050;
    }
}
