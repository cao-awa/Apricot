package com.github.cao.awa.apricot.network.packet.recevied.poke;

import com.github.cao.awa.apricot.event.receive.accomplish.poke.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;

public class PokeReceivedPacket extends ReadonlyPacket {
    private final MessageType type;
    private final long causerId;
    private final long botId;
    private final long targetId;
    private final long responseId;
    private final long timestamp;

    public PokeReceivedPacket(MessageType type, long causerId, long targetId, long botId, long responseId, long timestamp) {
        this.type = type;
        this.botId = botId;
        this.causerId = causerId;
        this.responseId = responseId;
        this.timestamp = timestamp;
        this.targetId = targetId;
    }

    public MessageType getType() {
        return this.type;
    }

    public long getCauserId() {
        return this.causerId;
    }

    public long getBotId() {
        return this.botId;
    }

    public long getTargetId() {
        return this.targetId;
    }

    public long getResponseId() {
        return this.responseId;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    /**
     * Let an event of this packet be fired.
     *
     * @param server
     *         apricot server
     * @param proxy
     *         apricot proxy
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void fireEvent(ApricotServer server, ApricotProxy proxy) {
        server.fireEvent(new PokeReceivedEvent(
                proxy,
                this
        ));
    }
}
