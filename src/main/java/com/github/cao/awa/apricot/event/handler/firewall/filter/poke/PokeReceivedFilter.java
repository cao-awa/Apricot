package com.github.cao.awa.apricot.event.handler.firewall.filter.poke;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.poke.*;

public abstract class PokeReceivedFilter extends FirewallEventHandler {
    /**
     * Filter the legitimated message to accepts.
     *
     * @param message the raw message
     * @return message legitimate
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract boolean legitimate(PokeReceivedEvent message);
}
