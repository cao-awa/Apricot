package com.github.cao.awa.apricot.event.receive.accomplish.response;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.handler.accomplish.response.*;
import com.github.cao.awa.apricot.event.handler.firewall.*;
import com.github.cao.awa.apricot.event.receive.accomplish.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.util.*;

public class EchoResultEvent extends Event<EchoResultPacket> {
    private static final Set<String> TARGETS = EntrustEnvironment.operation(
            ApricotCollectionFactor.newHashSet(),
            set -> set.add("echo-result")
    );

    public EchoResultEvent(ApricotProxy proxy, EchoResultPacket packet) {
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
        if (handler instanceof EchoResultEventHandler resultHandler) {
            resultHandler.onResult(this);
        }
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
        return true;
    }
}
