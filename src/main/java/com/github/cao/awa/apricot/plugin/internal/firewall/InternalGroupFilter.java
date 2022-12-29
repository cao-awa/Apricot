package com.github.cao.awa.apricot.plugin.internal.firewall;

import com.github.cao.awa.apricot.event.handler.firewall.filter.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;

import java.util.*;

public class InternalGroupFilter extends MessageIncludeFilter {
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
            return message.getPacket()
                          .getType() == SendMessageType.GROUP && (message.getPacket()
                                                                         .getResponseId() != 114514);
        } catch (Exception e) {
            return false;
        }
    }
}
