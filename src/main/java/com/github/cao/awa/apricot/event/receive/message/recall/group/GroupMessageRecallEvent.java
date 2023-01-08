package com.github.cao.awa.apricot.event.receive.message.recall.group;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.message.recall.group.*;
import com.github.cao.awa.apricot.event.receive.message.recall.*;
import com.github.cao.awa.apricot.network.packet.receive.message.recall.group.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class GroupMessageRecallEvent extends MessageRecallEvent<GroupMessageRecallPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> {
                set.add("notice-recall");
                set.add("notice-group-recall");
            }
    );

    public GroupMessageRecallEvent(ApricotProxy proxy, GroupMessageRecallPacket packet) {
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
    public void fireAccomplish(EventHandler handler) {
        if (handler instanceof GroupMessageRecallEventHandler recallHandler) {
            recallHandler.onRecall(this);
        }
        super.fireAccomplish(handler);
    }
}
