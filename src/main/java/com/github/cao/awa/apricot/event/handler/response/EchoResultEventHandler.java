package com.github.cao.awa.apricot.event.handler.response;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.response.*;

public abstract class EchoResultEventHandler extends EventHandler {
    @Override
    public final String getName() {
        return "echo-result";
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
    public abstract void onResult(EchoResultEvent event);
}
