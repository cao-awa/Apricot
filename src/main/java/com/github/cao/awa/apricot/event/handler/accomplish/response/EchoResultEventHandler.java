package com.github.cao.awa.apricot.event.handler.accomplish.response;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.response.*;

public abstract class EchoResultEventHandler extends AccomplishEventHandler {
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
