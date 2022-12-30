package com.github.cao.awa.apricot.network.packet.recevied.member.change.decrease.kick;

import com.github.cao.awa.apricot.event.receive.accomplish.member.change.decrease.bot.*;
import com.github.cao.awa.apricot.network.packet.recevied.member.change.decrease.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;

public class BotDiedFromGroupPacket extends GroupMemberDecreasedPacket {
    private final long operatorId;
    private final long botId;
    private final long userId;
    private final long groupId;
    private final long timestamp;

    public BotDiedFromGroupPacket(long operatorId, long botId, long userId, long groupId, long timestamp) {
        this.operatorId = operatorId;
        this.botId = botId;
        this.userId = userId;
        this.groupId = groupId;
        this.timestamp = timestamp;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public long getBotId() {
        return botId;
    }

    public long getUserId() {
        return userId;
    }

    public long getGroupId() {
        return groupId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Let an event of this packet be fired.
     *
     * @param server
     *         apricot server
     * @param proxy
     *         apricot proxy
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void fireEvent(ApricotServer server, ApricotProxy proxy) {
        server.fireEvent(new BotDiedFromGroupEvent(
                proxy,
                this
        ));
    }
}
