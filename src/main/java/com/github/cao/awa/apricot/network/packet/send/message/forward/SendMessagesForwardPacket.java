package com.github.cao.awa.apricot.network.packet.send.message.forward;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.forward.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.send.message.forward.group.*;
import com.github.cao.awa.apricot.network.packet.send.message.forward.personal.*;
import com.github.cao.awa.apricot.network.packet.writer.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class SendMessagesForwardPacket extends WritablePacket {
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

    public void compoundType(Function<MessageType, MessageType> function) {
        setType(function.apply(getType()));
    }

    private MessageType getType() {
        return type;
    }

    private void setType(MessageType type) {
        this.type = type;
    }

    public void compoundUserId(Function<Long, Long> function) {
        setUserId(function.apply(getUserId()));
    }

    private long getUserId() {
        return this.userId;
    }

    private void setUserId(long userId) {
        this.userId = userId;
    }

    public void compoundMessage(Function<List<ForwardMessage>, List<ForwardMessage>> function) {
        setMessages(function.apply(getMessages()));
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
    public boolean shouldEcho() {
        return true;
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
}
