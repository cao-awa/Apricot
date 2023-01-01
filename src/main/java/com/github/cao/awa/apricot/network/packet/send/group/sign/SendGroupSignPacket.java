package com.github.cao.awa.apricot.network.packet.send.group.sign;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.writer.*;

public class SendGroupSignPacket extends WritablePacket {
    private long groupId;

    public SendGroupSignPacket(long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean shouldEcho() {
        return true;
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
}
