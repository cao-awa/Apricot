package com.github.cao.awa.apricot.network.packet.send.message.group;

import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.message.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class SendGroupMessagePacket extends WritablePacket<SendMessageResponsePacket> {
    private long groupId;
    private @NotNull AssembledMessage message;
    private boolean autoEscape = false;

    public SendGroupMessagePacket(@NotNull AssembledMessage message, long groupId) {
        this.message = message;
        this.groupId = groupId;
    }

    public SendGroupMessagePacket(@NotNull AssembledMessage message, long groupId, boolean autoEscape) {
        this.message = message;
        this.groupId = groupId;
        this.autoEscape = autoEscape;
    }

    public boolean isAutoEscape() {
        return autoEscape;
    }

    public void setAutoEscape(boolean autoEscape) {
        this.autoEscape = autoEscape;
    }

    private long getGroupId() {
        return this.groupId;
    }

    private void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @NotNull
    private AssembledMessage getMessage() {
        return this.message;
    }

    private void setMessage(@NotNull AssembledMessage message) {
        this.message = message;
    }

    @Override
    public void write(PacketJSONBufWriter writer) {
        // Final child packet, write and flush it.
        this.message.incinerateMessage(message -> {
            new SendGroupMessagePacket(
                    message,
                    this.groupId,
                    this.autoEscape
            ).writeAndFlush(writer);
        });
        writer.take()
              .fluentPut(
                      "action",
                      "send_group_msg"
              );
        writer.take("params")
              .fluentPut(
                      "message",
                      this.message.toPlainText()
              )
              .fluentPut(
                      "group_id",
                      this.groupId
              )
              .fluentPut(
                      "auto_escape",
                      this.autoEscape
              );
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
