package com.github.cao.awa.apricot.event.handler.firewall.filter.mute.lift.personal;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.mute.issue.personal.*;
import com.github.cao.awa.apricot.event.receive.accomplish.mute.lift.personal.*;

public abstract class LiftGroupPersonalMuteFilter extends FirewallEventHandler {
    /**
     * Filter the legitimated event to accepts.
     *
     * @param event event
     * @return event legitimate
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract boolean legitimate(LiftGroupPersonalMuteEvent event);
}
