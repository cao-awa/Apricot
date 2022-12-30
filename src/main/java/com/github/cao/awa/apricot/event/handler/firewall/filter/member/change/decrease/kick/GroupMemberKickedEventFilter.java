package com.github.cao.awa.apricot.event.handler.firewall.filter.member.change.decrease.kick;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.member.change.decrease.kick.*;

public abstract class GroupMemberKickedEventFilter extends FirewallEventHandler {
    /**
     * Filter the legitimated event to accepts.
     *
     * @param event event
     * @return event legitimate
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract boolean legitimate(GroupMemberKickedEvent event);
}
