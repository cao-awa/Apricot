package com.github.cao.awa.apricot.network.packet.recevied.poke;

import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.poke.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.sender.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.server.*;

public class PokeReceivedPacket extends ReadonlyPacket {
    private final SendMessageType type;
    private final long causerId;
    private final long botId;
    private final long targetId;
    private final long responseId;
    private final long timestamp;

    public PokeReceivedPacket(SendMessageType type, long causerId, long targetId, long botId, long responseId, long timestamp) {
        this.type = type;
        this.botId = botId;
        this.causerId = causerId;
        this.responseId = responseId;
        this.timestamp = timestamp;
        this.targetId = targetId;
    }

    public SendMessageType getType() {
        return type;
    }

    public long getCauserId() {
        return causerId;
    }

    public long getBotId() {
        return botId;
    }

    public long getTargetId() {
        return targetId;
    }

    public long getResponseId() {
        return responseId;
    }

    public long getTimestamp() {
        return timestamp;
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
