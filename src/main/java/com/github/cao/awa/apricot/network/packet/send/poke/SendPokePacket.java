package com.github.cao.awa.apricot.network.packet.send.poke;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.element.cq.poke.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.group.*;
import com.github.cao.awa.apricot.network.packet.send.message.personal.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class SendPokePacket extends WritablePacket<SendMessageResponsePacket> {
    private MessageType type;
    private long targetId;
    private long botId;
    private long responseId;

    public SendPokePacket(@NotNull MessageType type, long targetId, long botId, long responseId) {
        this.type = type;
        this.targetId = targetId;
        this.botId = botId;
        this.responseId = responseId;
    }

    private MessageType getType() {
        return type;
    }

    private void setType(MessageType type) {
        this.type = type;
    }

    private long getTargetId() {
        return this.targetId;
    }

    private void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public long getBotId() {
        return this.botId;
    }

    public void setBotId(long botId) {
        this.botId = botId;
    }

    public long getResponseId() {
        return responseId;
    }

    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    @Override
    public void write(PacketJSONBufWriter writer) {
        writer.take()
              .fluentPut(
                      "action",
                      "send_msg"
              );
        if (this.type == MessageType.PRIVATE) {
            new SendPrivateMessagePacket(
                    new PokeMessageElement(this.targetId).toMessage(),
                    this.responseId,
                    0,
                    false
            ).write(writer);
        } else {
            new SendGroupMessagePacket(
                    new PokeMessageElement(this.targetId).toMessage(),
                    this.responseId,
                    false
            ).write(writer);
        }
    }

    @Override
    public boolean shouldEcho() {
        return true;
    }

    @Override
    public void send(ApricotProxy proxy, Consumer<SendMessageResponsePacket> response) {
        proxy.echo(
                this,
                result -> response.accept(new SendMessageResponsePacket(result.getData()
                                                                              .getLong("message_id")))
        );
    }
}
