package com.github.cao.awa.apricot.network.packet.send.group.kick;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;

import java.util.function.*;

public class SendGroupKickPacket extends WritablePacket<NoResponsePacket> {
    private boolean reject;
    private long groupId;
    private long userId;

    public SendGroupKickPacket(long groupId, long userId, boolean reject) {
        this.reject = reject;
        this.groupId = groupId;
        this.userId = userId;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean getReject() {
        return this.reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

    /**
     * Write data to buffer.
     *
     * @param writer
     *         writer
     * @author cao_awa
     * @since 1.0.0
     */
    @Override
    public void write(PacketJSONBufWriter writer) {
        writer.take()
              .fluentPut(
                      "action",
                      "set_group_kick"
              );
        writer.take("params")
              .fluentPut(
                      "group_id",
                      this.groupId
              )
              .fluentPut(
                      "user_id",
                      this.userId
              )
              .fluentPut(
                      "reject_add_request",
                      this.reject
              );
    }

    @Override
    public boolean shouldEcho() {
        return false;
    }

    @Override
    public void send(ApricotProxy proxy, Consumer<NoResponsePacket> response) {
        proxy.echo(
                this,
                result -> response.accept(NoResponsePacket.NO_RESPONSE)
        );
    }
}
