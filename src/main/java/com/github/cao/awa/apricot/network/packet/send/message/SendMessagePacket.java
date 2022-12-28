package com.github.cao.awa.apricot.network.packet.send.message;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class SendMessagePacket extends Packet {
    private SendMessageType type;
    private long userId;
    private long groupId;
    private String message;
    private boolean autoEscape = false;

    public SendMessagePacket(@NotNull SendMessageType type, @NotNull String message, long userId, long groupId) {
        this.type = type;
        this.userId = userId;
        this.groupId = groupId;
        this.message = message;
    }

    public SendMessagePacket(@NotNull SendMessageType type, @NotNull String message, long userId, long groupId, boolean autoEscape) {
        this.type = type;
        this.userId = userId;
        this.groupId = groupId;
        this.message = message;
        this.autoEscape = autoEscape;
    }

    public SendMessagePacket(@NotNull SendMessageType type, @NotNull String message, long userId) {
        this.type = type;
        this.userId = userId;
        this.message = message;
    }

    public SendMessagePacket(@NotNull SendMessageType type, @NotNull String message, long userId, boolean autoEscape) {
        this.type = type;
        this.userId = userId;
        this.message = message;
        this.autoEscape = autoEscape;
    }

    public void compoundType(Function<SendMessageType, SendMessageType> function) {
        setType(function.apply(getType()));
    }

    private SendMessageType getType() {
        return type;
    }

    private void setType(SendMessageType type) {
        this.type = type;
    }

    public void compoundId(Function<Long, Long> function) {
        setUserId(function.apply(getUserId()));
    }

    private long getUserId() {
        return this.userId;
    }

    private void setUserId(long userId) {
        this.userId = userId;
    }

    public void compoundMessage(Function<String, String> function) {
        setMessage(function.apply(getMessage()));
    }

    private String getMessage() {
        return this.message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void write(PacketJSONBufWriter writer) {
        writer.take()
              .put(
                      "action",
                      "send_msg"
              );
        if (this.type == SendMessageType.PRIVATE) {
            new SendPrivateMessagePacket(
                    this.message,
                    this.userId,
                    this.groupId,
                    this.autoEscape
            ).write(writer);
        } else {
            new SendGroupMessagePacket(
                    this.message,
                    this.userId,
                    this.autoEscape
            ).write(writer);
        }
    }
}
