package com.github.cao.awa.apricot.network.packet.send.message.personal;

import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.message.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class SendPrivateMessagePacket extends WritablePacket<SendMessageResponsePacket> {
    private long userId;
    private long groupId = - 1;
    private @NotNull AssembledMessage message;
    private boolean autoEscape = false;

    public SendPrivateMessagePacket(@NotNull AssembledMessage message, long userId) {
        this.message = message;
        this.userId = userId;
    }

    public SendPrivateMessagePacket(@NotNull AssembledMessage message, long userId, long groupId) {
        this.message = message;
        this.userId = userId;
        this.groupId = groupId;
    }

    public SendPrivateMessagePacket(@NotNull AssembledMessage message, long userId, long groupId, boolean autoEscape) {
        this.message = message;
        this.userId = userId;
        this.groupId = groupId;
        this.autoEscape = autoEscape;
    }

    public SendPrivateMessagePacket(@NotNull AssembledMessage message, long userId, boolean autoEscape) {
        this.message = message;
        this.userId = userId;
        this.autoEscape = autoEscape;
    }

    public boolean isAutoEscape() {
        return autoEscape;
    }

    public void setAutoEscape(boolean autoEscape) {
        this.autoEscape = autoEscape;
    }

    private long getUserId() {
        return this.userId;
    }

    private void setUserId(long id) {
        this.userId = id;
    }

    private long getGroupId() {
        return this.groupId;
    }

    private void setGroupId(long id) {
        this.groupId = id;
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
            new SendPrivateMessagePacket(
                    message,
                    this.userId,
                    this.groupId,
                    this.autoEscape
            ).writeAndFlush(writer);
        });
        writer.take()
              .fluentPut(
                      "action",
                      "send_private_msg"
              );
        writer.take("params")
              .fluentPut(
                      "message",
                      this.message.toPlainText()
              )
              .fluentPut(
                      "user_id",
                      this.userId
              )
              .fluentPut(
                      "auto_escape",
                      this.autoEscape
              );
        if (this.groupId != - 1) {
            writer.take("params")
                  .put(
                          "group_id",
                          this.groupId
                  );
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
