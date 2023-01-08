package com.github.cao.awa.apricot.event.receive.message.group.sent;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.message.sent.group.*;
import com.github.cao.awa.apricot.network.packet.receive.message.group.sent.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class GroupAnonymousMessageSentEvent extends GroupMessageSentEvent<GroupAnonymousMessageSentPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> {
                set.add("message-sent");
                set.add("message-sent-group");
                set.add("message-sent-group-anonymous");
            }
    );

    public GroupAnonymousMessageSentEvent(ApricotProxy proxy, GroupAnonymousMessageSentPacket packet) {
        super(
                proxy,
                packet
        );
    }

    @Override
    public final Set<String> pipeline() {
        return TARGETS;
    }

    /**
     * Fire event, let event entrust handler to process self.
     *
     * @param handler
     *         handler
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void fireEvent(EventHandler handler) {
        if (handler instanceof GroupAnonymousMessageSentEventHandler messageReceivedHandler) {
            messageReceivedHandler.onMessageSent(this);
        }
        super.fireEvent(handler);
    }
}
