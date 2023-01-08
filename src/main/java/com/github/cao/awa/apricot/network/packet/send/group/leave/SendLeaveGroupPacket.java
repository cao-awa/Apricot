package com.github.cao.awa.apricot.network.packet.send.group.leave;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;

import java.util.function.*;

public class SendLeaveGroupPacket extends WritablePacket<NoResponsePacket> {
    private boolean dismiss;
    private long groupId;

    public SendLeaveGroupPacket(long groupId, boolean dismiss) {
        this.dismiss = dismiss;
        this.groupId = groupId;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public boolean getDismiss() {
        return this.dismiss;
    }

    public void setDismiss(boolean dismiss) {
        this.dismiss = dismiss;
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
                      "set_group_leave"
              );
        writer.take("params")
              .fluentPut(
                      "group_id",
                      this.groupId
              )
              .fluentPut(
                      "is_dismiss",
                      this.dismiss
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
