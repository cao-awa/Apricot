package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.event.handler.accomplish.poke.*;
import com.github.cao.awa.apricot.event.receive.accomplish.poke.*;
import com.github.cao.awa.apricot.network.packet.recevied.poke.*;
import com.github.cao.awa.apricot.network.packet.send.poke.*;
import com.github.cao.awa.apricot.network.router.*;

public class PokeReciprocity extends PokeReceivedEventHandler {
    /**
     * Process event.
     *
     * @param event
     *         event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void onPoke(PokeReceivedEvent event) {
        PokeReceivedPacket packet = event.getPacket();
        ApricotProxy proxy = event.getProxy();
        if (packet.getTargetId() == packet.getBotId() && ! (packet.getCauserId() == packet.getBotId())) {
            proxy.send(new SendPokePacket(
                    packet.getType(),
                    packet.getCauserId(),
                    packet.getBotId(),
                    packet.getResponseId()
            ));
        }
    }
}
