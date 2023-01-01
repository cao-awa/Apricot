package com.github.cao.awa.apricot.event.handler.firewall.filter.mute.lift.all;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.mute.issue.all.*;
import com.github.cao.awa.apricot.event.receive.accomplish.mute.lift.all.*;

public abstract class LiftGroupAllMuteFilter extends FirewallEventHandler {
    /**
     * Filter the legitimated event to accepts.
     *
     * @param event event
     * @return event legitimate
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract boolean legitimate(LiftGroupAllMuteEvent event);
}
