package com.github.cao.awa.apricot.event.handler.lifecycle.disconnect;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.lifecycle.*;

public abstract class ProxyConnectEventHandler extends EventHandler<ProxyConnectEvent> {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public String getType() {
        return "meta-lifecycle-connect";
    }

    /**
     * Process event.
     *
     * @param event
     *         event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    public abstract void onConnect(ProxyConnectEvent event);
}
