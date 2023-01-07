package com.github.cao.awa.apricot.network.packet.receive.poke;

import com.github.cao.awa.apricot.event.receive.accomplish.poke.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.target.*;

public class GroupPokeReceivedPacket extends PokeReceivedPacket {
    private final long causerId;
    private final long botId;
    private final long targetId;
    private final long responseId;
    private final long timestamp;

    public GroupPokeReceivedPacket(long causerId, long targetId, long botId, long responseId, long timestamp) {
        this.botId = botId;
        this.causerId = causerId;
        this.responseId = responseId;
        this.timestamp = timestamp;
        this.targetId = targetId;
    }

    public MessageType getType() {
        return MessageType.GROUP;
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
        server.fireEvent(new GroupPokeReceivedEvent(
                proxy,
                this
        ));
    }

    @Override
    public EventTarget target() {
        return new EventTarget(
                this.responseId,
                this.causerId,
                this.botId
        );
    }
}
