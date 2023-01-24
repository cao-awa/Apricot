package com.github.cao.awa.apricot.network.packet.receive.mute.issue.all;

import com.github.cao.awa.apricot.event.receive.mute.issue.all.*;
import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.network.packet.receive.mute.issue.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;

public class IssueGroupAllMuteReceivedPacket extends IssueGroupMuteReceivedPacket {
    private final long botId;
    private final long groupId;
    private final long operatorId;
    private final long timestamp;

    public IssueGroupAllMuteReceivedPacket(long botId, long groupId, long operatorId, long timestamp) {
        this.botId = botId;
        this.groupId = groupId;
        this.operatorId = operatorId;
        this.timestamp = timestamp;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public long getBotId() {
        return this.botId;
    }

    public long getOperatorId() {
        return this.operatorId;
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
        server.fireEvent(new IssueGroupAllMuteEvent(
                proxy,
                this
        ));
    }

    @Override
    public EventTarget target() {
        return new EventTarget(this.groupId, this.operatorId, this.botId);
    }
}
