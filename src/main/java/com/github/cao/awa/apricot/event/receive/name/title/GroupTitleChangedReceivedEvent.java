package com.github.cao.awa.apricot.event.receive.name.title;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.name.title.*;
import com.github.cao.awa.apricot.event.receive.*;
import com.github.cao.awa.apricot.network.packet.receive.name.title.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

@Unsupported
@Warning("NOT_AFTER_TESTING")
public class GroupTitleChangedReceivedEvent extends Event<GroupTitleChangedReceivedPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(ApricotCollectionFactor.newHashSet(), set -> {
        set.add("notice-notify-title");
    });

    public GroupTitleChangedReceivedEvent(ApricotProxy proxy, GroupTitleChangedReceivedPacket packet) {
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
        if (handler instanceof GroupTitleChangedReceivedEventHandler changedHandler) {
            changedHandler.onChanged(this);
        }
    }
}
