package com.github.cao.awa.apricot.event.handler.message.recall.personal;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.message.recall.personal.*;

public abstract class PrivateMessageRecallEventHandler extends EventHandler {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public final String getType() {
        return "notice-friend-recall";
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
    public abstract void onRecall(PrivateMessageRecallEvent event);
}
