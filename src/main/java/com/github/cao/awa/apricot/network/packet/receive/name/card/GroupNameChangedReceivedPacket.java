package com.github.cao.awa.apricot.network.packet.receive.name.card;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.event.receive.accomplish.name.card.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.target.*;

/**
 * The name card change maybe not real time.<br>
 * <br>
 * If the proxy to qq is 'cq-http' then this event will be happened under user sent at least one messages.<br>
 * The not real time warning is only against for 'cq-http'<br>
 */
@Warning("NOT_REAL_TIME")
public class GroupNameChangedReceivedPacket extends ReadonlyPacket {
    private final String oldCard;
    private final String newCard;
    private final long botId;
    private final long groupId;
    private final long userId;
    private final long timestamp;

    public GroupNameChangedReceivedPacket(String oldCard, String newCard, long botId, long userId, long groupId, long timestamp) {
        this.oldCard = oldCard;
        this.newCard = newCard;
        this.botId = botId;
        this.userId = userId;
        this.groupId = groupId;
        this.timestamp = timestamp;
    }

    public String getOldCard() {
        return this.oldCard;
    }

    public String getNewCard() {
        return this.newCard;
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
        server.fireEvent(new GroupNameChangedReceivedEvent(
                proxy,
                this
        ));
    }

    @Override
    public EventTarget target() {
        return new EventTarget(this.groupId, this.userId, this.botId);
    }
}
