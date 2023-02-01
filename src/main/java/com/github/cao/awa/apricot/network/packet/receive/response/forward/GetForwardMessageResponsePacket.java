package com.github.cao.awa.apricot.network.packet.receive.response.forward;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.server.*;

@Unsupported
public class GetForwardMessageResponsePacket extends ResponsePacket {
    public static GetForwardMessageResponsePacket create(ApricotServer server, JSONObject json) {
        System.out.println(json);
        return null;
    }
}
