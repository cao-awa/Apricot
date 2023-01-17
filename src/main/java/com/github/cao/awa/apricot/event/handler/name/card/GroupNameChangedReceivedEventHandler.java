package com.github.cao.awa.apricot.event.handler.name.card;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.event.receive.name.card.*;

/**
 * The name card change maybe not real time.<br>
 * <br>
 * If the proxy to qq is 'cq-http' then this event will be happened under user sent at least one messages.<br>
 * The not real time warning is only against for 'cq-http'<br>
 */
@Warning("NOT_REAL_TIME")
public abstract class GroupNameChangedReceivedEventHandler extends EventHandler<GroupNameChangedReceivedEvent> {
    /**
     * Which type event is target of this handler.
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public String getType() {
        return "notice-group-card";
    }

    /**
     * Process event.
     *
     * @param event
     *         event
     * @author 草二号机
     * @since 1.0.0
     */
    public abstract void onChanged(GroupNameChangedReceivedEvent event);
}
