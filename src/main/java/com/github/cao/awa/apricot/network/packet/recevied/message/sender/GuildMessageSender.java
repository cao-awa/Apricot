package com.github.cao.awa.apricot.network.packet.recevied.message.sender;

import com.github.cao.awa.apricot.anntations.*;

@Unsupported
public class GuildMessageSender implements MessageSender {
    @Override
    public int getAge() {
        return 0;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getSex() {
        return "";
    }

    @Override
    public long getUserId() {
        return -1;
    }
}
