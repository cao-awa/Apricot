package com.github.cao.awa.apricot.network.packet.receive.message.sender;

public class BasicMessageSender implements MessageSender {
    private final long senderId;
    private final String nickname;

    public BasicMessageSender(long senderId, String nickname) {
        this.senderId = senderId;
        this.nickname = nickname;
    }

    public BasicMessageSender(long senderId) {
        this.senderId = senderId;
        this.nickname = "";
    }

    public String getNickname() {
        return this.nickname;
    }

    @Override
    public long getSenderId() {
        return this.senderId;
    }
}
