package com.github.cao.awa.apricot.network.packet.send.poke;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.element.cq.element.poke.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.send.message.group.*;
import com.github.cao.awa.apricot.network.packet.send.message.personal.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class SendPokePacket extends WritablePacket {
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

    public void compoundType(Function<MessageType, MessageType> function) {
        setType(function.apply(getType()));
    }

    private MessageType getType() {
        return type;
    }

    private void setType(MessageType type) {
        this.type = type;
    }

    public void compoundTargetId(Function<Long, Long> function) {
        setTargetId(function.apply(getTargetId()));
    }

    private long getTargetId() {
        return this.targetId;
    }

    private void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public void compoundBotId(Function<Long, Long> function) {
        setBotId(function.apply(getBotId()));
    }

    public long getBotId() {
        return this.botId;
    }

    public void setBotId(long botId) {
        this.botId = botId;
    }

    public void compoundResponseId(Function<Long, Long> function) {
        setResponseId(function.apply(getResponseId()));
    }

    public long getResponseId() {
        return responseId;
    }

    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    @Override
    public boolean shouldEcho() {
        return true;
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
                    new PokeMessageElement(
                            this.targetId,
                            this.botId
                    ).toMessage(),
                    this.responseId,
                    0,
                    false
            ).write(writer);
        } else {
            new SendGroupMessagePacket(
                    new PokeMessageElement(
                            this.targetId,
                            this.botId
                    ).toMessage(),
                    this.responseId,
                    false
            ).write(writer);
        }
    }
}
