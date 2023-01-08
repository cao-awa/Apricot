package com.github.cao.awa.apricot.network.packet.send.recall;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;

import java.util.function.*;

public class SendRecallMessagePacket extends WritablePacket<NoResponsePacket> {
    private long messageId;

    public SendRecallMessagePacket(long messageId) {
        this.messageId = messageId;
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
