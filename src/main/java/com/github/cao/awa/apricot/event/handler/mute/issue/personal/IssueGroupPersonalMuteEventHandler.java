package com.github.cao.awa.apricot.event.handler.mute.issue.personal;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.mute.issue.personal.*;

public abstract class IssueGroupPersonalMuteEventHandler extends EventHandler<IssueGroupPersonalMuteEvent> {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public String getType() {
        return "notice-group-ban-personal";
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
    public abstract void onMute(IssueGroupPersonalMuteEvent event);
}
