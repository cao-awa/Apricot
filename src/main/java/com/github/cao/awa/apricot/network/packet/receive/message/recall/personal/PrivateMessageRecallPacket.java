package com.github.cao.awa.apricot.network.packet.receive.message.recall.personal;

import com.github.cao.awa.apricot.event.receive.message.recall.personal.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.recall.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.target.*;

public class PrivateMessageRecallPacket extends MessageRecallPacket {
    private final long botId;
    private final long responseId;
    private final long timestamp;
    private final long messageId;

    public PrivateMessageRecallPacket(long botId, long responseId, long timestamp, long messageId) {
        this.botId = botId;
        this.responseId = responseId;
        this.timestamp = timestamp;
        this.messageId = messageId;
    }

    @Override
    public MessageType getType() {
        return MessageType.PRIVATE;
    }

    public long getMessageId() {
        return this.messageId;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public long getBotId() {
        return this.botId;
    }

    public long getResponseId() {
        return this.responseId;
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
        server.fireEvent(new PrivateMessageRecallEvent(
                proxy,
                this
        ));
    }

    @Override
    public EventTarget target() {
        return new EventTarget(
                - 1,
                this.responseId,
                this.botId
        );
    }
}
