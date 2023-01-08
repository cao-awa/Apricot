package com.github.cao.awa.apricot.network.packet.send.group.sign;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;

import java.util.function.*;

public class SendGroupSignPacket extends WritablePacket<NoResponsePacket> {
    private long groupId;

    public SendGroupSignPacket(long groupId) {
        this.groupId = groupId;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
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
                      "send_group_sign"
              );
        writer.take("params")
              .fluentPut(
                      "group_id",
                      this.groupId
              );
    }

    @Override
    public boolean shouldEcho() {
        return true;
    }

    @Override
    public void send(ApricotProxy proxy, Consumer<NoResponsePacket> response) {
        proxy.echo(
                this,
                result -> response.accept(NoResponsePacket.NO_RESPONSE)
        );
    }
}
