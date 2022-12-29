package com.github.cao.awa.apricot.network.packet.send.poke;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.cq.element.poke.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class SendPokePacket extends Packet {
    private SendMessageType type;
    private long targetId;
    private long botId;

    public SendPokePacket(@NotNull SendMessageType type, long targetId, long botId) {
        this.type = type;
        this.targetId = targetId;
        this.botId = botId;
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
        setTargetId(function.apply(getTargetId()));
    }

    private long getTargetId() {
        return this.targetId;
    }

    private void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public long getBotId() {
        return this.botId;
    }

    public void setBotId(long botId) {
        this.botId = botId;
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
        if (this.type == SendMessageType.PRIVATE) {
            new SendPrivateMessagePacket(
                    new PokeMessageElement(
                            this.targetId,
                            this.botId
                    ).toMessage(),
                    this.targetId,
                    this.botId,
                    false
            ).write(writer);
        } else {
            new SendGroupMessagePacket(
                    new PokeMessageElement(
                            this.targetId,
                            this.botId
                    ).toMessage(),
                    this.targetId,
                    false
            ).write(writer);
        }
    }
}
