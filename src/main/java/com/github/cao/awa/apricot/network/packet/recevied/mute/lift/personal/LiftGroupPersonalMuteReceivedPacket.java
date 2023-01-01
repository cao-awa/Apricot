package com.github.cao.awa.apricot.network.packet.recevied.mute.lift.personal;

import com.github.cao.awa.apricot.event.receive.accomplish.mute.lift.personal.*;
import com.github.cao.awa.apricot.network.packet.recevied.mute.lift.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;

public class LiftGroupPersonalMuteReceivedPacket extends LiftGroupMuteReceivedPacket {
    private final long botId;
    private final long userId;
    private final long groupId;
    private final long operatorId;
    private final long timestamp;

    public LiftGroupPersonalMuteReceivedPacket(long botId, long userId, long groupId, long operatorId, long timestamp) {
        this.botId = botId;
        this.userId = userId;
        this.groupId = groupId;
        this.operatorId = operatorId;
        this.timestamp = timestamp;
    }

    public long getUserId() {
        return this.userId;
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
        server.fireEvent(new LiftGroupPersonalMuteEvent(
                proxy,
                this
        ));
    }
}
