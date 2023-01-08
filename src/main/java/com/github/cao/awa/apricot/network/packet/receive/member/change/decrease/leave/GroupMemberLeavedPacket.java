package com.github.cao.awa.apricot.network.packet.receive.member.change.decrease.leave;

import com.github.cao.awa.apricot.event.receive.member.change.decrease.leave.*;
import com.github.cao.awa.apricot.network.packet.receive.member.change.decrease.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.target.*;

public class GroupMemberLeavedPacket extends GroupMemberDecreasedPacket {
    private final long operatorId;
    private final long botId;
    private final long userId;
    private final long groupId;
    private final long timestamp;

    public GroupMemberLeavedPacket(long operatorId, long botId, long userId, long groupId, long timestamp) {
        this.operatorId = operatorId;
        this.botId = botId;
        this.userId = userId;
        this.groupId = groupId;
        this.timestamp = timestamp;
    }

    public long getOperatorId() {
        return this.operatorId;
    }

    public long getBotId() {
        return this.botId;
    }

    public long getUserId() {
        return this.userId;
    }

    public long getGroupId() {
        return this.groupId;
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
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void fireEvent(ApricotServer server, ApricotProxy proxy) {
        server.fireEvent(new GroupMemberLeavedEvent(
                proxy,
                this
        ));
    }

    @Override
    public EventTarget target() {
        return new EventTarget(
                this.groupId,
                this.userId,
                this.botId
        );
    }
}
