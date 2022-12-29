package com.github.cao.awa.apricot.network.packet.send.message;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class SendPrivateMessagePacket extends Packet {
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

    public void compoundAutoEscape(Function<Boolean, Boolean> function) {
        setAutoEscape(function.apply(isAutoEscape()));
    }

    public boolean isAutoEscape() {
        return autoEscape;
    }

    public void setAutoEscape(boolean autoEscape) {
        this.autoEscape = autoEscape;
    }

    public void compoundUserId(Function<Long, Long> function) {
        setUserId(function.apply(getUserId()));
    }

    private long getUserId() {
        return this.userId;
    }

    private void setUserId(long id) {
        this.userId = id;
    }

    public void compoundGroupId(Function<Long, Long> function) {
        setUserId(function.apply(getUserId()));
    }

    private long getGroupId() {
        return this.groupId;
    }

    private void setGroupId(long id) {
        this.groupId = id;
    }

    public void compoundMessage(Function<AssembledMessage, AssembledMessage> function) {
        setMessage(function.apply(getMessage()));
    }

    @NotNull
    private AssembledMessage getMessage() {
        return this.message;
    }

    private void setMessage(@NotNull AssembledMessage message) {
        this.message = message;
    }

    @Override
    public boolean shouldEcho() {
        return true;
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
}
