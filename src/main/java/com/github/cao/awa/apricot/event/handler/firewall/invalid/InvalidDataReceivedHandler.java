package com.github.cao.awa.apricot.event.handler.firewall.invalid;

import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.firewall.invalid.*;

public abstract class InvalidDataReceivedHandler extends FirewallEventHandler {
    /**
     * Filter the legitimated message to accepts.
     *
     * @param data the raw message
     * @return message legitimate
     *
     * @since 1.0.0
     * @author cao_awa
     */
    public abstract boolean legitimate(InvalidDataReceivedEvent data);
}
