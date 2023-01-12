package com.github.cao.awa.apricot.network.packet.receive.message.sender;

public interface IgnoredIdMessageSender extends MessageSender {
    int getAge();

    String getName();

    String getSex();

    long getUserId();

    @Override
    default long getSenderId() {
        return - 1;
    }
}
