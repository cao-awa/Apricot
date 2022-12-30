package com.github.cao.awa.apricot.event.handler.firewall.filter.name.title;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.name.title.*;

@Unsupported
public abstract class GroupTitleChangedReceivedEventFilter extends FirewallEventHandler {
    /**
     * Filter the legitimated event to accepts.
     *
     * @param event event
     * @return event legitimate
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract boolean legitimate(GroupTitleChangedReceivedEvent event);
}
