package com.github.cao.awa.apricot.plugin.ext.example.encrypt;

import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.message.forward.*;
import com.github.cao.awa.apricot.message.forward.dummy.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.send.forward.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.encryption.*;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.*;
import java.util.*;

public class Encrypt extends MessageEventHandler {
    private static final Random RANDOM = new Random();

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
        ApricotProxy proxy = event.getProxy();

        AssembledMessage message = packet.getMessage();

        String plain = message.toPlainText();

        if (plain.startsWith(".enc")) {
            List<ForwardMessage> messages = new ArrayList<>();

            try {
                String msg = plain.substring(plain.indexOf(" ") + 1);

                byte[] key = new byte[32];
                RANDOM.nextBytes(key);

                byte[] encrypted = AES.encrypt(
                        msg.getBytes(StandardCharsets.UTF_8),
                        key
                );

                messages.add(new DummyForwardMessage(
                        packet.getSenderId(),
                        packet.getSender()
                              .getName(),
                        new TextMessageElement("密文：" + Base64.encodeBase64String(encrypted)).toMessage()
                ));

                messages.add(new DummyForwardMessage(
                        packet.getSenderId(),
                        packet.getSender()
                              .getName(),
                        new TextMessageElement("密钥：" + Base64.encodeBase64String(key)).toMessage()
                ));
            } catch (Exception e) {
                StringBuilder trace = new StringBuilder();

                trace.append("加密失败：")
                     .append(e.getMessage());

                for (StackTraceElement element : e.getStackTrace()) {
                    trace.append("\n")
                         .append(element.toString());
                }

                messages.add(new DummyForwardMessage(
                        packet.getSenderId(),
                        packet.getSender()
                              .getName(),
                        new TextMessageElement(trace.toString()).toMessage()
                ));
            }
            proxy.send(new SendMessagesForwardPacket(
                    packet.getType(),
                    messages,
                    packet.getResponseId()
            ));
        }
    }
}
