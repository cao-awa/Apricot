package com.github.cao.awa.apricot.event.handler.mute.lift;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.mute.lift.*;

public abstract class LiftGroupMuteEventHandler extends EventHandler<LiftGroupMuteEvent<?>> {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public String getType() {
        return "notice-group-lift-ban";
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
    public abstract void onLift(LiftGroupMuteEvent<?> event);
}
