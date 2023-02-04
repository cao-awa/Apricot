package com.github.cao.awa.apricot.network.packet.send.message;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.group.*;
import com.github.cao.awa.apricot.network.packet.send.message.personal.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class SendMessagePacket extends WritablePacket<SendMessageResponsePacket> {
    private MessageType type;
    private long responseId;
    private AssembledMessage message;
    private boolean autoEscape = false;

    public SendMessagePacket(@NotNull MessageType type, @NotNull AssembledMessage message, long responseId) {
        this.type = type;
        this.responseId = responseId;
        this.message = message;
    }

    public SendMessagePacket(@NotNull MessageType type, @NotNull AssembledMessage message, long responseId, boolean autoEscape) {
        this.type = type;
        this.responseId = responseId;
        this.message = message;
        this.autoEscape = autoEscape;
    }

    private MessageType getType() {
        return type;
    }

    private void setType(MessageType type) {
        this.type = type;
    }

    public boolean isAutoEscape() {
        return autoEscape;
    }

    public void setAutoEscape(boolean autoEscape) {
        this.autoEscape = autoEscape;
    }

    private long getResponseId() {
        return this.responseId;
    }

    private void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    private AssembledMessage getMessage() {
        return this.message;
    }

    private void setMessage(AssembledMessage message) {
        this.message = message;
    }

    @Override
    public void write(PacketJSONBufWriter writer) {
        // This not final child packet, write but do not flush it.
        // The final child will flush.
        this.message.incinerateMessage(message -> {
            new SendMessagePacket(
                    this.type,
                    message,
                    this.responseId,
                    this.autoEscape
            ).write(writer);
        });
        writer.take()
              .fluentPut(
                      "action",
                      "send_msg"
              );
        if (this.type == MessageType.PRIVATE) {
            new SendPrivateMessagePacket(
                    this.message,
                    this.responseId,
                    - 1,
                    this.autoEscape
            ).write(writer);
        } else {
            new SendGroupMessagePacket(
                    this.message,
                    this.responseId,
                    this.autoEscape
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
                result -> response.accept(SendMessageResponsePacket.create(result.getData()))
        );
    }
}
