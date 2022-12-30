package com.github.cao.awa.apricot.event.handler.firewall.filter.name.title;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.name.card.*;
import com.github.cao.awa.apricot.event.receive.accomplish.name.title.*;

/**
 * The name card change maybe not real time.<br>
 * <br>
 * If the proxy to qq is 'cq-http' then this event will be happened under user sent at least one messages.<br>
 * The not real time warning is only against for 'cq-http'<br>
 */
public abstract class GroupTitleChangedReceivedEventFilter extends FirewallEventHandler {
    /**
     * Filter the legitimated message to accepts.
     *
     * @param event the raw message
     * @return message legitimate
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract boolean legitimate(GroupTitleChangedReceivedEvent event);
}
