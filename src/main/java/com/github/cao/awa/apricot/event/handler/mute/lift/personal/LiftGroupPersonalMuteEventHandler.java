package com.github.cao.awa.apricot.event.handler.mute.lift.personal;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.mute.lift.personal.*;

public abstract class LiftGroupPersonalMuteEventHandler extends EventHandler {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public String getType() {
        return "notice-group-lift-personal";
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
    public abstract void onLift(LiftGroupPersonalMuteEvent event);
}
