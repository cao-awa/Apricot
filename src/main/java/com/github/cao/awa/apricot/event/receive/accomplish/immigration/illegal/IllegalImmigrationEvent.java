package com.github.cao.awa.apricot.event.receive.accomplish.immigration.illegal;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.immigration.illegal.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class IllegalImmigrationEvent extends NoFirewallEvent {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> set.add("illegal-immigration")
    );
    private final Event<?> event;

    public IllegalImmigrationEvent(ApricotProxy proxy, Event<?> event) {
        super(
                proxy,
                event.getPacket()
        );
        this.event = event;
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
        if (handler instanceof IllegalImmigrationHandler illegalImmigrationHandler) {
            illegalImmigrationHandler.onIllegalImmigration(this);
        }
    }

    public Event<?> getIllegalEvent() {
        return event;
    }
}
