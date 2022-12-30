package com.github.cao.awa.apricot.network.packet.recevied.response;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.event.receive.accomplish.response.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.*;
import com.github.cao.awa.apricot.server.*;
import org.jetbrains.annotations.*;

public class EchoResultPacket extends ReadonlyPacket {
    private final String identifier;
    private final JSONObject response;

    public EchoResultPacket(String identifier, JSONObject response) {
        this.identifier = identifier;
        this.response = response;
    }

    public JSONObject getResponse() {
        return this.response;
    }

    @Nullable
    public String getIdentifier() {
        return this.identifier;
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
        server.fireEvent(new EchoResultEvent(
                proxy,
                this
        ));
    }
}
