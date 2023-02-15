package com.github.cao.awa.apricot.network.packet.send.forward.get;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.forward.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;

import java.util.function.*;

@Planning
public class GetForwardMessagePacket extends WritablePacket<GetForwardMessageResponsePacket> {
    public String messageId;

    public GetForwardMessagePacket(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
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
                      "get_forward_msg"
              );
        writer.take("params")
              .fluentPut(
                      "id",
                      this.messageId
              );
    }

    @Override
    public boolean shouldEcho() {
        return true;
    }

    @Override
    public void send(ApricotProxy proxy, Consumer<GetForwardMessageResponsePacket> response) {
        proxy.echo(
                this,
                result -> {
                    response.accept(GetForwardMessageResponsePacket.create(
                            proxy.server(),
                            result.getData()
                    ));
                }
        );
    }
}
