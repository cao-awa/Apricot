package com.github.cao.awa.apricot.plugin.ext.lawn.handler;

import com.github.cao.awa.apricot.event.handler.poke.*;
import com.github.cao.awa.apricot.event.receive.poke.*;
import com.github.cao.awa.apricot.network.packet.receive.poke.*;
import com.github.cao.awa.apricot.network.packet.send.poke.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.task.intensive.*;

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
    public void onPoke(PokeReceivedEvent<?> event) {
        PokeReceivedPacket packet = event.getPacket();
        ApricotProxy proxy = event.proxy();
        if (packet.getTargetId() == packet.getBotId() && ! (packet.getCauserId() == packet.getBotId())) {
            proxy.echo(new SendPokePacket(
                    packet.getType(),
                    packet.getCauserId(),
                    packet.getBotId(),
                    packet.getResponseId()
            ));
        }
    }

    /**
     * The poke reciprocity is intensive IO.
     *
     * @return Intensive
     */
    @Override
    public IntensiveType intensiveType() {
        return IntensiveType.IO;
    }
}
