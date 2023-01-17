package com.github.cao.awa.apricot.event.receive.mute.lift.personal;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.mute.lift.personal.*;
import com.github.cao.awa.apricot.event.receive.mute.lift.*;
import com.github.cao.awa.apricot.network.packet.receive.mute.lift.personal.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class LiftGroupPersonalMuteEvent extends LiftGroupMuteEvent<LiftGroupPersonalMuteReceivedPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> {
                set.add("notice-group-lift-ban");
                set.add("notice-group-lift-personal");
            }
    );

    public LiftGroupPersonalMuteEvent(ApricotProxy proxy, LiftGroupPersonalMuteReceivedPacket packet) {
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
        if (handler instanceof LiftGroupPersonalMuteEventHandler eventHandler) {
            if (this.isExclusive()) {
                eventHandler.onExclusive(this);
            } else {
                eventHandler.onLift(this);
            }
        }
        super.fireEvent(handler);
    }
}
