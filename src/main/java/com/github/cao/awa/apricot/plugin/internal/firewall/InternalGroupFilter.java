package com.github.cao.awa.apricot.plugin.internal.firewall;

import com.github.cao.awa.apricot.event.handler.firewall.filter.message.group.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.group.*;

public class InternalGroupFilter extends GroupNormalMessageReceivedFilter {
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
    public boolean legitimate(GroupNormalMessageReceivedEvent message) {
        return true;
    }
}
