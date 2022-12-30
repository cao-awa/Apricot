package com.github.cao.awa.apricot.event.handler.accomplish.name.card;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.name.card.*;

/**
 * The name card change maybe not real time.<br>
 * <br>
 * If the proxy to qq is 'cq-http' then this event will be happened under user sent at least one messages.<br>
 * The not real time warning is only against for 'cq-http'<br>
 */
public abstract class GroupNameChangedReceivedEventHandler extends AccomplishEventHandler {
    @Override
    public String getType() {
        return "notice-group-card";
    }

    public abstract void onChanged(GroupNameChangedReceivedEvent event);
}
