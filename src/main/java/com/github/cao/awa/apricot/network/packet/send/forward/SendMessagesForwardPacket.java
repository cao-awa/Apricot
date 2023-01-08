package com.github.cao.awa.apricot.network.packet.send.forward;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.forward.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.response.*;
import com.github.cao.awa.apricot.network.packet.send.forward.group.*;
import com.github.cao.awa.apricot.network.packet.send.forward.personal.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import com.github.cao.awa.apricot.network.router.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class SendMessagesForwardPacket extends WritablePacket<NoResponsePacket> {
    private MessageType type;
    private long userId;
    private long groupId;
    private List<ForwardMessage> messages;

    public SendMessagesForwardPacket(@NotNull MessageType type, @NotNull List<ForwardMessage> messages, long userId, long groupId) {
        this.type = type;
        this.userId = userId;
        this.groupId = groupId;
        this.messages = messages;
    }

    public SendMessagesForwardPacket(@NotNull MessageType type, @NotNull List<ForwardMessage> messages, long userId) {
        this.type = type;
        this.userId = userId;
        this.messages = messages;
    }

    private MessageType getType() {
        return type;
    }

    private void setType(MessageType type) {
        this.type = type;
    }

    private long getUserId() {
        return this.userId;
    }

    private void setUserId(long userId) {
        this.userId = userId;
    }

    private List<ForwardMessage> getMessages() {
        return this.messages;
    }

    private void setMessages(List<ForwardMessage> messages) {
        this.messages = messages;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Override
    public void write(PacketJSONBufWriter writer) {
        if (this.type == MessageType.PRIVATE) {
            new SendPrivateMessagesForwardPacket(
                    this.messages,
                    this.userId
            ).write(writer);
        } else {
            new SendGroupMessagesForwardPacket(
                    this.messages,
                    this.groupId
            ).write(writer);
        }
    }

    @Override
    public boolean shouldEcho() {
        return true;
    }

    @Override
    public void send(ApricotProxy proxy, Consumer<NoResponsePacket> response) {
        proxy.echo(
                this,
                result -> response.accept(NoResponsePacket.NO_RESPONSE)
        );
    }
}
