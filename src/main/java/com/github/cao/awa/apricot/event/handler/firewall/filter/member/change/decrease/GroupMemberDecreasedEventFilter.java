package com.github.cao.awa.apricot.event.handler.firewall.filter.member.change.decrease;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.member.change.decrease.*;
import com.github.cao.awa.apricot.event.receive.accomplish.member.change.increase.*;

public abstract class GroupMemberDecreasedEventFilter extends FirewallEventHandler {
    /**
     * Filter the legitimated event to accepts.
     *
     * @param event event
     * @return event legitimate
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract boolean legitimate(GroupMemberDecreasedEvent<?> event);
}
