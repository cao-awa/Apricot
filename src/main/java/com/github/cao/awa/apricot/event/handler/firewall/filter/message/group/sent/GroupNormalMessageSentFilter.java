package com.github.cao.awa.apricot.event.handler.firewall.filter.message.group.sent;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.group.received.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.group.sent.*;

public abstract class GroupNormalMessageSentFilter extends FirewallEventHandler {
    /**
     * Filter the legitimated event to accepts.
     *
     * @param event event
     * @return event legitimate
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract boolean legitimate(GroupNormalMessageSentEvent event);
}
