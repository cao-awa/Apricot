package com.github.cao.awa.apricot.network.packet.send.message;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.send.message.group.*;
import com.github.cao.awa.apricot.network.packet.send.message.personal.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import org.jetbrains.annotations.*;

public class SendMessagePacket extends WritablePacket {
    private MessageType type;
    private long userId;
    private long groupId;
    private AssembledMessage message;
    private boolean autoEscape = false;

    public SendMessagePacket(@NotNull MessageType type, @NotNull AssembledMessage message, long userId, long groupId) {
        this.type = type;
        this.userId = userId;
        this.groupId = groupId;
        this.message = message;
    }

    public SendMessagePacket(@NotNull MessageType type, @NotNull AssembledMessage message, long userId, long groupId, boolean autoEscape) {
        this.type = type;
        this.userId = userId;
        this.groupId = groupId;
        this.message = message;
        this.autoEscape = autoEscape;
    }

    public SendMessagePacket(@NotNull MessageType type, @NotNull AssembledMessage message, long userId) {
        this.type = type;
        this.userId = userId;
        this.message = message;
    }

    public SendMessagePacket(@NotNull MessageType type, @NotNull AssembledMessage message, long userId, boolean autoEscape) {
        this.type = type;
        this.userId = userId;
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

    private long getUserId() {
        return this.userId;
    }

    private void setUserId(long userId) {
        this.userId = userId;
    }

    private AssembledMessage getMessage() {
        return this.message;
    }

    private void setMessage(AssembledMessage message) {
        this.message = message;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean shouldEcho() {
        return true;
    }

    @Override
    public void write(PacketJSONBufWriter writer) {
        // This not final child packet, write but do not flush it.
        // The final child will flush.
        this.message.incinerateMessage(message -> {
            new SendMessagePacket(
                    this.type,
                    message,
                    this.userId,
                    this.groupId,
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
                    this.userId,
                    this.groupId,
                    this.autoEscape
            ).write(writer);
        } else {
            new SendGroupMessagePacket(
                    this.message,
                    this.groupId,
                    this.autoEscape
            ).write(writer);
        }
    }
}
