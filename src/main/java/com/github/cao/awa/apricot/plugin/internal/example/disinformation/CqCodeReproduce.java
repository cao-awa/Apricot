package com.github.cao.awa.apricot.plugin.internal.example.disinformation;

import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.forward.*;
import com.github.cao.awa.apricot.message.forward.dummy.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.send.forward.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.cao.awa.apricot.util.message.*;

import java.util.*;

public class CqCodeReproduce extends MessageEventHandler {
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

        String msg = MessageUtil.unescape(plain.substring(plain.indexOf(" ") + 1)
                                               .strip()
                                               .trim());
        if (plain.startsWith(".cqs")) {
            SendMessagePacket send = new SendMessagePacket(
                    packet.getType(),
                    MessageUtil.process(
                            proxy.server(),
                            msg
                    ),
                    packet.getResponseId()
            );

            proxy.send(send);
        } else if (plain.startsWith(".cq")) {
            List<ForwardMessage> msgs = ApricotCollectionFactor.newArrayList();

            try {
                DummyForwardMessage dummyMessage = new DummyForwardMessage(
                        packet.getSenderId(),
                        packet.getSender()
                              .getName(),
                        MessageUtil.process(
                                proxy.server(),
                                msg
                        )
                );
                msgs.add(dummyMessage);
            } catch (Exception e) {

            }

            SendMessagesForwardPacket forward = new SendMessagesForwardPacket(
                    packet.getType(),
                    msgs,
                    packet.getResponseId(),
                    packet.getResponseId()
            );

            proxy.send(forward);
        }
    }
}