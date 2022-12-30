package com.github.cao.awa.apricot.network.packet.recevied.message.recall.group;

import com.github.cao.awa.apricot.event.receive.accomplish.message.recall.group.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.recall.*;
import com.github.cao.awa.apricot.server.*;

public class GroupMessageRecallPacket extends MessageRecallPacket {
    private final long botId;
    private final long responseId;
    private final long timestamp;
    private final long operatorId;
    private final long groupId;
    private final long messageId;

    public GroupMessageRecallPacket(long botId, long responseId, long timestamp, long operatorId, long groupId, long messageId) {
        this.botId = botId;
        this.responseId = responseId;
        this.timestamp = timestamp;
        this.operatorId = operatorId;
        this.groupId = groupId;
        this.messageId = messageId;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public long getOperatorId() {
        return this.operatorId;
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
        server.fireEvent(new GroupMessageRecallEvent(
                proxy,
                this
        ));
    }
}
