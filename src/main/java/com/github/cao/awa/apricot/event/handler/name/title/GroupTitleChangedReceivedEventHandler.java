package com.github.cao.awa.apricot.event.handler.name.title;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.name.title.*;

public abstract class GroupTitleChangedReceivedEventHandler extends EventHandler {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public String getType() {
        return "notice-notify-title";
    }

    /**
     * Process event.
     *
     * @param event
     *         event
     * @author 草二号机
     * @since 1.0.0
     */
    public abstract void onChanged(GroupTitleChangedReceivedEvent event);
}
