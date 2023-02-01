package com.github.cao.awa.apricot.plugin.internal.example.disinformation;

import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.cq.xml.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.message.*;

public class XmlMaker extends MessageEventHandler {
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
    public void onMessage(MessageEvent<?> event) {
        MessagePacket packet = event.getPacket();
        AssembledMessage message = packet.getMessage();
        ApricotProxy proxy = event.getProxy();

        String plain = message.toPlainText();

        if (plain.startsWith(".xml")) {
            String msg = MessageUtil.unlw(MessageUtil.unescape(plain.substring(plain.indexOf(" ") + 1)
                                                                    .strip()
                                                                    .trim()));

            SendMessagePacket send = new SendMessagePacket(
                    packet.getType(),
                    new XmlMessageElement(msg).toMessage(),
                    packet.getResponseId()
            );

            proxy.send(send);
        }
    }
}