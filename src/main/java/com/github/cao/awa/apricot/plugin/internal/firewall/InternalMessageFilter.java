package com.github.cao.awa.apricot.plugin.internal.firewall;

import com.github.cao.awa.apricot.event.handler.firewall.filter.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.message.*;

import java.util.*;

public class InternalMessageFilter extends MessageIncludeFilter {
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
        try {
            return Objects.requireNonNull(message.getPacket()
                                                 .getMessage()
                                                 .get(
                                                         0,
                                                         TextMessageElement.class
                                                 )
                                                 .getText())
                          .contains("awa");
        } catch (Exception e) {
            return true;
        }
    }
}
