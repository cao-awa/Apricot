package com.github.cao.awa.apricot.network.packet.send.message;

import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class SendGroupMessagePacket extends Packet {
    private long userId;
    private @NotNull String message;
    private boolean autoEscape = false;

    public SendGroupMessagePacket(@NotNull String message, long userId) {
        this.message = message;
        this.userId = userId;
    }

    public SendGroupMessagePacket(@NotNull String message, long userId, boolean autoEscape) {
        this.message = message;
        this.userId = userId;
        this.autoEscape = autoEscape;
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
                      "send_group_msg"
              );
        writer.take("params")
              .fluentPut(
                      "message",
                      this.message
              )
              .fluentPut(
                      "group_id",
                      this.userId
              )
              .fluentPut(
                      "auto_escape",
                      this.autoEscape
              );
    }
}
