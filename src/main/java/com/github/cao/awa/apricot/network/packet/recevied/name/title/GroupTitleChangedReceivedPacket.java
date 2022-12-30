package com.github.cao.awa.apricot.network.packet.recevied.name.title;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.event.receive.accomplish.name.title.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;

/**
 * The name card change maybe not real time.<br>
 * <br>
 * If the proxy to qq is 'cq-http' then this event will be happened under user sent at least one messages.<br>
 * The not real time warning is only against for 'cq-http'<br>
 */
@Warning("NOT_REAL_TIME")
public class GroupTitleChangedReceivedPacket extends ReadonlyPacket {
    private final String title;
    private final long botId;
    private final long groupId;
    private final long userId;
    private final long timestamp;

    public GroupTitleChangedReceivedPacket(String title, long botId, long userId, long groupId, long timestamp) {
        this.title = title;
        this.botId = botId;
        this.userId = userId;
        this.groupId = groupId;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return this.title;
    }

    public long getBotId() {
        return this.botId;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public long getUserId() {
        return this.userId;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    /**
     * Let an event of this packet be fired.
     *
     * @param server
     *         apricot server
     * @param proxy
     *         apricot proxy
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void fireEvent(ApricotServer server, ApricotProxy proxy) {
        server.fireEvent(new GroupTitleChangedReceivedEvent(
                proxy,
                this
        ));
    }
}
