package com.github.cao.awa.apricot.network.packet.send.message.recall;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.writer.*;

import java.util.function.*;

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

    public void compoundMessageId(Function<Long, Long> function) {
        setMessageId(function.apply(getMessageId()));
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
