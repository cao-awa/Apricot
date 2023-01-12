package com.github.cao.awa.apricot.network.packet.receive.response.message.get;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.message.*;

@Unsupported
public class GetMessageResponsePacket extends ResponsePacket {
    private final BasicMessageSender sender;
    private final AssembledMessage message;
    private final long timestamp;

    public GetMessageResponsePacket(BasicMessageSender sender, AssembledMessage message, long timestamp) {
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static GetMessageResponsePacket create(ApricotServer server, JSONObject json) {
        return new GetMessageResponsePacket(
                new BasicMessageSender(
                        json.getLong("user_id"),
                        json.getString("nickname")
                ),
                MessageUtil.process(
                        server,
                        json.getString("message")
                ),
                json.getLong("time")
        );
    }

    public BasicMessageSender getSender() {
        return sender;
    }
}
