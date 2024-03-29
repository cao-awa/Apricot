package com.github.cao.awa.apricot.plugin.ext.lawn.handler;

import com.github.cao.awa.apricot.event.handler.message.received.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.message.forward.*;
import com.github.cao.awa.apricot.message.forward.dummy.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.send.forward.*;
import com.github.cao.awa.apricot.network.packet.send.group.name.card.*;
import com.github.cao.awa.apricot.network.packet.send.recall.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.cao.awa.apricot.util.time.*;
import org.apache.logging.log4j.*;

import java.util.*;

public class Tests extends MessageReceivedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("InternalGroup");

    /**
     * Process event.
     *
     * @param event event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent<?> event) {
        ApricotProxy proxy = event.proxy();
        MessageReceivedPacket packet = event.getPacket();
        ApricotServer server = proxy.server();

        if (event.getPacket()
                 .getMessage()
                 .toPlainText()
                 .equals(".test_recall")) {
            event.proxy()
                 .send(
                         new SendRecallMessagePacket(event.getPacket()
                                                          .getMessageSeq()),
                         result -> {
                         }
                 );
        }

        if (event.getPacket()
                 .getMessage()
                 .toPlainText()
                 .equals(".dummy_forward")) {
            List<ForwardMessage> messages = ApricotCollectionFactor.arrayList();

            messages.add(new DummyForwardMessage(
                    packet.getSenderId(),
                    packet.getSender()
                          .getName(),
                    TextMessageElement.text("草")
                                      .toMessage()
            ));

            event.proxy()
                 .send(
                         new SendMessagesForwardPacket(
                                 packet.getType(),
                                 messages,
                                 packet.getResponseId(),
                                 packet.getResponseId()
                         ),
                         result -> {
                         }
                 );
        }

        if (event.getPacket()
                 .getMessage()
                 .toPlainText()
                 .equals(".change_name")) {
            proxy.echo(new SetGroupNameCardPacket(
                    String.valueOf(TimeUtil.millions()),
                    packet.getResponseId(),
                    packet.getSenderId()
            ));
        }
    }
}
