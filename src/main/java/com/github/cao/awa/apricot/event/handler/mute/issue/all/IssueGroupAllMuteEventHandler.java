package com.github.cao.awa.apricot.event.handler.mute.issue.all;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.mute.issue.all.*;

public abstract class IssueGroupAllMuteEventHandler extends EventHandler<IssueGroupAllMuteEvent> {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public String getType() {
        return "notice-group-ban-all";
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
    public abstract void onMute(IssueGroupAllMuteEvent event);
}
