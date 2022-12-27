package com.github.cao.awa.bot.network.packet.send.message;

import com.github.cao.awa.bot.network.packet.*;
import com.github.cao.awa.bot.network.packet.writer.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class SendPrivateMessagePacket extends Packet {
    private long userId;
    private long groupId = - 1;
    private @NotNull String message;
    private boolean autoEscape = false;

    public SendPrivateMessagePacket(@NotNull String message, long userId) {
        this.message = message;
        this.userId = userId;
    }

    public SendPrivateMessagePacket(@NotNull String message, long userId, long groupId) {
        this.message = message;
        this.userId = userId;
        this.groupId = groupId;
    }

    public SendPrivateMessagePacket(@NotNull String message, long userId, long groupId, boolean autoEscape) {
        this.message = message;
        this.userId = userId;
        this.groupId = groupId;
        this.autoEscape = autoEscape;
    }

    public SendPrivateMessagePacket(@NotNull String message, long userId, boolean autoEscape) {
        this.message = message;
        this.userId = userId;
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

    public void compoundMessage(Function<String, String> function) {
        setMessage(function.apply(getMessage()));
    }

    @NotNull
    private String getMessage() {
        return this.message;
    }

    private void setMessage(@NotNull String message) {
        this.message = message;
    }

    @Override
    public void write(PacketJSONBufWriter writer) {
        writer.take()
              .put(
                      "action",
                      "send_private_msg"
              );
        writer.take("params")
              .fluentPut(
                      "message",
                      this.message
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
