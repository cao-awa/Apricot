package com.github.cao.awa.apricot.event.receive.message.recall.personal;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.message.recall.personal.*;
import com.github.cao.awa.apricot.event.receive.message.recall.*;
import com.github.cao.awa.apricot.network.packet.receive.message.recall.personal.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
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
    public void fireEvent(EventHandler<?> handler) {
        if (handler instanceof PrivateMessageRecallEventHandler eventHandler) {
            if (this.isExclusive()) {
                eventHandler.onExclusive(this);
            } else {
                eventHandler.onRecall(this);
            }
        }
        super.fireEvent(handler);
    }
}
