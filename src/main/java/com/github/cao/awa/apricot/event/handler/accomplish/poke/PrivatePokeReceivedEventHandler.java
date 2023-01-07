package com.github.cao.awa.apricot.event.handler.accomplish.poke;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.poke.*;

public abstract class PrivatePokeReceivedEventHandler extends AccomplishEventHandler {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public final String getType() {
        return "notice-poke-private";
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
    public abstract void onPoke(PrivatePokeReceivedEvent event);
}
