package com.github.cao.awa.apricot.network.packet.receive.add.friend;

import com.github.cao.awa.apricot.event.receive.add.friend.*;
import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.send.add.friend.approve.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class AddFriendReceivedPacket extends ReadonlyPacket {
    private final String comment;
    private final long botId;
    private final long userId;
    private final long timestamp;
    private final String flag;

    public AddFriendReceivedPacket(String comment, long botId, long userId, long timestamp, String flag) {
        this.comment = comment;
        this.botId = botId;
        this.userId = userId;
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

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getFlag() {
        return this.flag;
    }

    public void rapid(ApricotProxy proxy, boolean approve, @Nullable String remark) {
        proxy.echo(new SendFriendApprovalPacket(
                this.flag,
                approve,
                remark
        ));
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
        server.fireEvent(new AddFriendEvent(
                proxy,
                this
        ));
    }

    @Override
    public EventTarget target() {
        return new EventTarget(
                - 1,
                this.userId,
                this.botId
        );
    }
}
