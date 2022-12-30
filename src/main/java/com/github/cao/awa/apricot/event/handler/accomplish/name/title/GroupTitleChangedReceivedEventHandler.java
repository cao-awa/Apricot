package com.github.cao.awa.apricot.event.handler.accomplish.name.title;

import com.github.cao.awa.apricot.event.handler.accomplish.*;
import com.github.cao.awa.apricot.event.receive.accomplish.name.card.*;
import com.github.cao.awa.apricot.event.receive.accomplish.name.title.*;

/**
 * The name card change maybe not real time.<br>
 * <br>
 * If the proxy to qq is 'cq-http' then this event will be happened under user sent at least one messages.<br>
 * The not real time warning is only against for 'cq-http'<br>
 */
public abstract class GroupTitleChangedReceivedEventHandler extends AccomplishEventHandler {
    @Override
    public String getType() {
        return "notice-notify-title";
    }

    public abstract void onChanged(GroupTitleChangedReceivedEvent event);
}
