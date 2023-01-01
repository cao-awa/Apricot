package com.github.cao.awa.apricot.network.packet.send.group.recall;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.writer.*;

public class SendRecallMessagePacket extends WritablePacket {
    private long messageId;

    public SendRecallMessagePacket(long messageId) {
        this.messageId = messageId;
    }

    @Override
    public boolean shouldEcho() {
        return true;
    }

    public long getMessageId() {
        return this.messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
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
                      "delete_msg"
              );
        writer.take("params")
              .fluentPut(
                      "message_id",
                      this.messageId
              );
    }
}
