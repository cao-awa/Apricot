package com.github.cao.awa.apricot.network.packet.send.message.get;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.message.get.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;

import java.util.function.*;

public class GetMessagePacket extends WritablePacket<GetMessageResponsePacket> {
    public int messageId;

    public GetMessagePacket(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
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
                      "get_msg"
              );
        writer.take("params")
              .fluentPut(
                      "message_id",
                      this.messageId
              );
    }

    @Override
    public boolean shouldEcho() {
        return false;
    }

    @Override
    public void send(ApricotProxy proxy, Consumer<GetMessageResponsePacket> response) {
        proxy.echo(
                this,
                result -> {
                    response.accept(GetMessageResponsePacket.create(
                            proxy.server(),
                            result.getData()
                    ));
                }
        );
    }
}
