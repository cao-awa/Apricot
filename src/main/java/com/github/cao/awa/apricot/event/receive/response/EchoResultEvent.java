package com.github.cao.awa.apricot.event.receive.response;

import com.github.cao.awa.apricot.event.*;
import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.handler.response.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.recevied.response.*;

public class EchoResultEvent extends Event<EchoResultPacket> {
    public EchoResultEvent(ApricotProxy proxy, EchoResultPacket packet) {
        super(proxy, packet);
    }

    @Override
    public String getName() {
        return "echo-result";
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
    public void entrust(EventHandler handler) {
        if (handler instanceof EchoResultEventHandler resultHandler) {
            resultHandler.onResult(this);
        }
    }
}
