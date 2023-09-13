package com.github.cao.awa.apricot.plugin.ext.example.disinformation;

import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.forward.*;
import com.github.cao.awa.apricot.message.forward.dummy.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.send.forward.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.cao.awa.apricot.util.message.*;
import com.github.cao.awa.apricot.util.text.*;

import java.util.*;

public class Disinformation extends MessageEventHandler {
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
        ApricotProxy proxy = event.proxy();

        String plain = message.toPlainText();

        if (plain.startsWith(".disinformation") || plain.startsWith(".dis")) {
            int space = plain.indexOf(" ");
            int lineWrap = plain.indexOf("\n");
            List<String> args = TextUtil.splitToList(
                    plain.substring(lineWrap == - 1 ? space : lineWrap)
                         .strip()
                         .trim(),
                    '\n'
            );

            List<ForwardMessage> msgs = ApricotCollectionFactor.arrayList();

            for (String arg : args) {
                arg = MessageUtil.unescape(arg);

                try {
                    int numberIndex = arg.indexOf(" ");
                    if (numberIndex == - 1) {
                        continue;
                    }
                    int nameIndex = arg.indexOf(
                            " ",
                            numberIndex + 1
                    );
                    if (nameIndex == - 1) {
                        continue;
                    }
                    String number = arg.substring(
                                               0,
                                               numberIndex
                                       )
                                       .strip()
                                       .trim();
                    String name = arg.substring(
                                             numberIndex + 1,
                                             nameIndex
                                     )
                                     .strip()
                                     .trim();
                    int msgIndex = nameIndex + 1;
                    String msg = arg.substring(msgIndex)
                                    .strip()
                                    .trim();
                    DummyForwardMessage dummyMessage = new DummyForwardMessage(
                            Long.parseLong(number),
                            name,
                            MessageUtil.process(
                                    proxy.server(),
                                    msg
                            )
                    );
                    msgs.add(dummyMessage);
                } catch (Exception e) {

                }
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
