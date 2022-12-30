package com.github.cao.awa.apricot.event.receive.accomplish.message.recall.personal;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.message.recall.personal.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.handler.firewall.filter.message.recall.personal.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.recall.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.recall.personal.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class PrivateMessageRecallEvent extends MessageRecallEvent<PrivateMessageRecallPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> {
                set.add("notice-recall");
                set.add("notice-friend-recall");
            }
    );

    public PrivateMessageRecallEvent(ApricotProxy proxy, PrivateMessageRecallPacket packet) {
        super(
                proxy,
                packet
        );
    }

    @Override
    public Set<String> pipeline() {
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
        if (handler instanceof PrivateMessageRecallEventHandler recallHandler) {
            recallHandler.onRecall(this);
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
        if (handler instanceof PrivateMessageRecallFilter recallFilter) {
            return recallFilter.legitimate(this) && super.fireFirewall(handler);
        }
        return true;
    }
}
