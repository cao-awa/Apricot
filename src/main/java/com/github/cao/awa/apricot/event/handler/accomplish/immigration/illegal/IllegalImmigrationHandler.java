package com.github.cao.awa.apricot.event.handler.accomplish.immigration.illegal;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.immigration.illegal.*;

public abstract class IllegalImmigrationHandler extends AccomplishEventHandler {
    @Override
    public final String getName() {
        return "illegal-immigration";
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
    public abstract void onIllegalImmigration(IllegalImmigrationEvent event);
}
