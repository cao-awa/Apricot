package com.github.cao.awa.apricot.event.receive.mute.issue.all;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.mute.issue.all.*;
import com.github.cao.awa.apricot.event.receive.mute.issue.*;
import com.github.cao.awa.apricot.network.packet.receive.mute.issue.all.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class IssueGroupAllMuteEvent extends IssueGroupMuteEvent<IssueGroupAllMuteReceivedPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> {
                set.add("notice-group-ban-ban");
                set.add("notice-group-ban-all");
            }
    );

    public IssueGroupAllMuteEvent(ApricotProxy proxy, IssueGroupAllMuteReceivedPacket packet) {
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
    public void fireEvent(EventHandler handler) {
        if (handler instanceof IssueGroupAllMuteEventHandler eventHandler) {
            eventHandler.onMute(this);
        }
        super.fireEvent(handler);
    }
}
