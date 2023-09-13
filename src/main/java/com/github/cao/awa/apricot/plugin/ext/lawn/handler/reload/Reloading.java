package com.github.cao.awa.apricot.plugin.ext.lawn.handler.reload;

import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.cq.replay.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.message.*;

public class Reloading extends MessageEventHandler {
    /**
     * Process event.
     *
     * @param event event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void onMessage(MessageEvent<?> event) {
        MessagePacket packet = event.getPacket();
        ApricotProxy proxy = event.proxy();
        AssembledMessage message = packet.getMessage();
        ApricotServer server = proxy.server();

        if (MessageProcess.command(
                message,
                "..reload"
        )) {
            server.apsConfig()
                  .reload();

            server.getPlugins()
                  .forEach(Plugin :: reload);

            proxy.send(new SendMessagePacket(
                    packet.getType(),
                    AssembledMessage.of()
                                    .participate(ReplyMessageElement.reply(packet.getMessageSeq()))
                                    .participate(TextMessageElement.text("OK")),
                    packet.getResponseId()
            ));
        }
    }
}
