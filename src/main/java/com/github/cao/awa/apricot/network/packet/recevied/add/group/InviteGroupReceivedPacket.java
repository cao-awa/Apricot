package com.github.cao.awa.apricot.network.packet.recevied.add.group;

import com.github.cao.awa.apricot.approval.group.*;
import com.github.cao.awa.apricot.event.receive.accomplish.add.group.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;

public class InviteGroupReceivedPacket extends ReadonlyPacket {
    private final String comment;
    private final long botId;
    private final long userId;
    private final long groupId;
    private final long timestamp;
    private final String flag;

    public InviteGroupReceivedPacket(String comment, long botId, long userId, long groupId, long timestamp, String flag) {
        this.comment = comment;
        this.botId = botId;
        this.userId = userId;
        this.groupId = groupId;
        this.timestamp = timestamp;
        this.flag = flag;
    }

    public String getComment() {
        return this.comment;
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

    public String getFlag() {
        return this.flag;
    }

    public ApprovalType getApprovalType() {
        return ApprovalType.INVITE;
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
        server.fireEvent(new InviteGroupEvent(
                proxy,
                this
        ));
    }
}
