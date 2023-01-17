package com.github.cao.awa.apricot.event.receive.lifecycle;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.lifecycle.disconnect.*;
import com.github.cao.awa.apricot.event.receive.*;
import com.github.cao.awa.apricot.network.packet.receive.meta.lifecycle.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class ProxyDisconnectEvent extends Event<ProxyDisconnectPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> {
                set.add("meta-lifecycle-disconnect");
            }
    );

    public ProxyDisconnectEvent(ApricotProxy proxy, ProxyDisconnectPacket packet) {
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
        if (handler instanceof ProxyDisconnectEventHandler eventHandler) {
            if (this.isExclusive()) {
                eventHandler.onExclusive(this);
            } else {
                eventHandler.onDisconnect(this);
            }
        }
    }
}
