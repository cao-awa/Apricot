package com.github.cao.awa.apricot.event.handler.firewall.filter.member.change.increase.invite;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.member.change.increase.approve.*;
import com.github.cao.awa.apricot.event.receive.accomplish.member.change.increase.invite.*;

public abstract class GroupMemberInvitedEventFilter extends FirewallEventHandler {
    /**
     * Filter the legitimated event to accepts.
     *
     * @param event event
     * @return event legitimate
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract boolean legitimate(GroupMemberInvitedEvent event);
}
