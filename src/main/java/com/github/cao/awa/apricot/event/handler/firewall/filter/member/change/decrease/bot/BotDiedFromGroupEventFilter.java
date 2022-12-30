package com.github.cao.awa.apricot.event.handler.firewall.filter.member.change.decrease.bot;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.member.change.decrease.bot.*;

public abstract class BotDiedFromGroupEventFilter extends FirewallEventHandler {
    /**
     * Filter the legitimated event to accepts.
     *
     * @param event event
     * @return event legitimate
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract boolean legitimate(BotDiedFromGroupEvent event);
}
