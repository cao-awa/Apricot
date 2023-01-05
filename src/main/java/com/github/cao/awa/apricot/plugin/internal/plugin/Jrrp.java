package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.event.handler.accomplish.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.element.at.*;
import com.github.cao.awa.apricot.message.element.cq.element.at.target.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;

import java.text.*;
import java.util.*;

public class Jrrp extends MessageReceivedEventHandler {
    private static final DateFormat format = new SimpleDateFormat("yyyMMdd");
    private static final Random random = new Random();

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
    public void onMessageReceived(MessageReceivedEvent<?> event) {
        MessageReceivedPacket packet = event.getPacket();
        ApricotProxy proxy = event.getProxy();

        String command = packet.getMessage()
                               .toPlainText();

        if (!(command.startsWith(".") || command.startsWith("。") || command.startsWith("/"))) {
            return;
        }

        command = command.substring(1);

        String date = format.format(new Date());

        // Bad method to calculate jrrp.
        long time = Long.parseLong(date);

        long rp = (time ^ packet.getSenderId() | (time * 2));

        while (rp > 100) {
            rp = (rp >> 1);
        }

        switch (command) {
            case "jrrp" -> {
                AssembledMessage message = new AssembledMessage();
                if (packet.getType() == MessageType.GROUP) {
                    message.participate(new AtMessageElement(new AtTarget(
                            AtTargetType.PERSON,
                            packet.getSenderId(),
                            packet.getSender()
                                  .getName()
                    )));
                }

                message.participate(new TextMessageElement(" 你的今日人品是: " + rp));

                proxy.send(new SendMessagePacket(
                        packet.getType(),
                        message,
                        packet.getResponseId()
                ));
            }
            case "rand" -> {
                AssembledMessage message = new AssembledMessage();
                if (packet.getType() == MessageType.GROUP) {
                    message.participate(new AtMessageElement(new AtTarget(
                            AtTargetType.PERSON,
                            packet.getSenderId(),
                            packet.getSender()
                                  .getName()
                    )));
                }

                message.participate(new TextMessageElement(" 一个随机数: " + random.nextInt(-1, 100001)));

                proxy.send(new SendMessagePacket(
                        packet.getType(),
                        message,
                        packet.getResponseId()
                ));
            }
        }
    }
}
