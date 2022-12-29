package com.github.cao.awa.apricot.network.packet.recevied.invalid;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.event.receive.firewall.invalid.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.server.*;

public class InvalidDataReceivedPacket extends ReadonlyPacket {
    private final JSONObject request;
    private final boolean handleFailed;

    public InvalidDataReceivedPacket(JSONObject request, boolean handleFailed) {
        this.request = request;
        this.handleFailed = handleFailed;
    }

    public JSONObject getRequest() {
        return request;
    }

    /**
     * Let an event of this packet be fired.
     *
     * @param server
     *         apricot server
     * @param proxy
     *         apricot proxy
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void fireEvent(ApricotServer server, ApricotProxy proxy) {
        server.fireEvent(new InvalidDataReceivedEvent(
                proxy,
                this
        ));
    }
}
