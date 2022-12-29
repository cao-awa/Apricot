package com.github.cao.awa.apricot.plugin.internal.firewall;

import com.github.cao.awa.apricot.event.handler.firewall.filter.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;

public class InternalMessageFilter extends MessageReceivedFilter {
    /**
     * Filter the legitimated message to accepts.
     *
     * @param message
     *         the raw message
     * @return message legitimate
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public boolean legitimate(MessageReceivedEvent message) {
        return true;
    }
}
