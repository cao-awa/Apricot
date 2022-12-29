package com.github.cao.awa.apricot.plugin.internal.firewall;

import com.github.cao.awa.apricot.event.handler.firewall.invalid.*;
import com.github.cao.awa.apricot.event.receive.firewall.invalid.*;

public class InternalInvalidDataHandler extends InvalidDataReceivedHandler {
    /**
     * Filter the legitimated message to accepts.
     *
     * @param data
     *         the raw message
     * @return message legitimate
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public boolean legitimate(InvalidDataReceivedEvent data) {
        return false;
    }
}
