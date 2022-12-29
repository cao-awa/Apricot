package com.github.cao.awa.apricot.network.packet.send.message;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class SendGroupMessagePacket extends Packet {
    private long userId;
    private @NotNull AssembledMessage message;
    private boolean autoEscape = false;

    public SendGroupMessagePacket(@NotNull AssembledMessage message, long userId) {
        this.message = message;
        this.userId = userId;
    }

    public SendGroupMessagePacket(@NotNull AssembledMessage message, long userId, boolean autoEscape) {
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

    public void compoundId(Function<Long, Long> function) {
        setUserId(function.apply(getUserId()));
    }

    private long getUserId() {
        return this.userId;
    }

    private void setUserId(long userId) {
        this.userId = userId;
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
            new SendGroupMessagePacket(
                    message,
                    this.userId,
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
                      this.userId
              )
              .fluentPut(
                      "auto_escape",
                      this.autoEscape
              );
    }
}
