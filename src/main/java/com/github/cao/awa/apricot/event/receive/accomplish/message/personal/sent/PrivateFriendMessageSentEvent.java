package com.github.cao.awa.apricot.event.receive.accomplish.message.personal.sent;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.message.sent.personal.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.message.personal.sent.*;
import com.github.cao.awa.apricot.network.packet.receive.message.personal.sent.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class PrivateFriendMessageSentEvent extends PrivateMessageSentEvent<PrivateFriendMessageSentPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> {
                set.add("message-sent");
                set.add("message-sent-private");
                set.add("message-sent-private-friend");
            }
    );

    public PrivateFriendMessageSentEvent(ApricotProxy proxy, PrivateFriendMessageSentPacket packet) {
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
    public void fireAccomplish(AccomplishEventHandler handler) {
        if (handler instanceof PrivateFriendMessageSentEventHandler messageReceivedHandler) {
            messageReceivedHandler.onMessageSent(this);
        }
        super.fireAccomplish(handler);
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
    public boolean fireFirewall(FirewallEventHandler handler) {
        if (handler instanceof PrivateFriendMessageSentFilter messageFilter) {
            return messageFilter.legitimate(this) && super.fireFirewall(handler);
        }
        return true;
    }
}
