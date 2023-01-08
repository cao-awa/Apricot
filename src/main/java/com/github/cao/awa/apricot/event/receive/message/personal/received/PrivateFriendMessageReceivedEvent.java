package com.github.cao.awa.apricot.event.receive.message.personal.received;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.message.received.personal.*;
import com.github.cao.awa.apricot.network.packet.receive.message.personal.received.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class PrivateFriendMessageReceivedEvent extends PrivateMessageReceivedEvent<PrivateFriendMessageReceivedPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> {
                set.add("message-received");
                set.add("message-private");
                set.add("message-private-friend");
            }
    );

    public PrivateFriendMessageReceivedEvent(ApricotProxy proxy, PrivateFriendMessageReceivedPacket packet) {
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
        if (handler instanceof PrivateFriendMessageReceivedEventHandler messageReceivedHandler) {
            messageReceivedHandler.onMessageReceived(this);
        }
        super.fireEvent(handler);
    }
}
