package com.github.cao.awa.apricot.event.handler.poke;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.poke.*;

public abstract class GroupPokeReceivedEventHandler extends EventHandler<GroupPokeReceivedEvent> {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public final String getType() {
        return "notice-poke-group";
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
    public abstract void onPoke(GroupPokeReceivedEvent event);
}
