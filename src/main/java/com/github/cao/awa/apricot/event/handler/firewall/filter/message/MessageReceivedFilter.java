package com.github.cao.awa.apricot.event.handler.firewall.filter.message;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.personal.*;

public abstract class MessageReceivedFilter extends FirewallEventHandler {
    /**
     * Filter the legitimated event to accepts.
     *
     * @param event event
     * @return event legitimate
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract boolean legitimate(MessageReceivedEvent<?> event);
}
