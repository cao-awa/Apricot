package com.github.cao.awa.apricot.network.packet.receive.response.forward;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.message.forward.dummy.node.DummyForwardTree;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.server.*;

@Unsupported({"TIMESTAMP"})
public class GetForwardMessageResponsePacket extends ResponsePacket {
    private final DummyForwardTree messages;

    public GetForwardMessageResponsePacket(DummyForwardTree messages) {
        this.messages = messages;
    }

    public DummyForwardTree getMessages() {
        return this.messages;
    }

    public static GetForwardMessageResponsePacket create(ApricotServer server, JSONObject json) {
        return new GetForwardMessageResponsePacket(DummyForwardTree.create(server, json));
    }
}
