package com.github.cao.awa.apricot.plugin.internal.example.encrypt;

import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.forward.*;
import com.github.cao.awa.apricot.message.forward.dummy.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.send.forward.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.encryption.*;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.*;
import java.util.*;

public class Decrypt extends MessageEventHandler {
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

        if (plain.startsWith(".dec")) {
            List<ForwardMessage> messages = new ArrayList<>();

            try {
                String msg = plain.substring(plain.indexOf(" ") + 1);
                String encrypted = msg.substring(
                        0,
                        msg.indexOf(" ")
                );
                String key = msg.substring(msg.indexOf(" ") + 1);

                String decrypted = new String(
                        AES.decrypt(
                                Base64.decodeBase64(encrypted.getBytes(StandardCharsets.UTF_8)),
                                Base64.decodeBase64(key.getBytes(StandardCharsets.UTF_8))
                        ),
                        StandardCharsets.UTF_8
                );

                messages.add(new DummyForwardMessage(
                        packet.getSenderId(),
                        packet.getSender()
                              .getName(),
                        new TextMessageElement("密文：" + encrypted).toMessage()
                ));

                messages.add(new DummyForwardMessage(
                        packet.getSenderId(),
                        packet.getSender()
                              .getName(),
                        new TextMessageElement("密钥：" + key).toMessage()
                ));

                messages.add(new DummyForwardMessage(
                        packet.getSenderId(),
                        packet.getSender()
                              .getName(),
                        new TextMessageElement("结果：" + decrypted).toMessage()
                ));
            } catch (Exception e) {
                StringBuilder trace = new StringBuilder();

                trace.append("解密失败：")
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
