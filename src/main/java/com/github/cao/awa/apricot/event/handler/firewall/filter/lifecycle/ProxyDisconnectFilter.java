package com.github.cao.awa.apricot.event.handler.firewall.filter.lifecycle;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.lifecycle.*;

public abstract class ProxyDisconnectFilter extends FirewallEventHandler {
    /**
     * Filter the legitimated event to accepts.
     *
     * @param event event
     * @return event legitimate
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract boolean legitimate(ProxyDisconnectEvent event);
}
