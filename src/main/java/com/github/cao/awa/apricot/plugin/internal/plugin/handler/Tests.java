package com.github.cao.awa.apricot.plugin.internal.plugin.handler;

import com.github.cao.awa.apricot.event.handler.message.received.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.forward.*;
import com.github.cao.awa.apricot.message.forward.dummy.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.send.forward.*;
import com.github.cao.awa.apricot.network.packet.send.group.name.card.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.packet.send.recall.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.server.service.counter.traffic.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.cao.awa.apricot.util.time.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle.*;
import org.apache.logging.log4j.*;

import java.util.*;

public class Tests extends MessageReceivedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("InternalGroup");

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
        ApricotProxy proxy = event.getProxy();
        MessageReceivedPacket packet = event.getPacket();
        ApricotServer server = proxy.server();

        if (event.getPacket()
                 .getMessage()
                 .toPlainText()
                 .equals(".test_recall")) {
            event.getProxy()
                 .send(
                         new SendRecallMessagePacket(event.getPacket()
                                                          .getMessageId()),
                         result -> {
                         }
                 );
        }

        if (event.getPacket()
                 .getMessage()
                 .toPlainText()
                 .equals(".traffic")) {
            TrafficCounter traffics = server.getTrafficsCounter();
            TrafficCounter packets = server.getPacketsCounter();

            StringBuilder builder = new StringBuilder();
            builder.append("--Traffic--")
                   .append("\n");
            builder.append("In: ")
                   .append(traffics.getTotalIn())
                   .append(" Bytes")
                   .append("\n");
            builder.append("Out: ")
                   .append(traffics.getTotalOut())
                   .append(" Bytes")
                   .append("\n");
            builder.append("--Packets--")
                   .append("\n");
            builder.append("In: ")
                   .append(packets.getTotalIn())
                   .append("\n");
            builder.append("Out: ")
                   .append(packets.getTotalOut())
                   .append("\n");
            Legacy<Long, Long> trafficsOneSecond = traffics.current();
            Legacy<Long, Long> packetsOneSecond = packets.current();
            builder.append("--Traffic (1s)--")
                   .append("\n");
            builder.append("In: ")
                   .append(trafficsOneSecond.newly())
                   .append(" Bytes")
                   .append("\n");
            builder.append("Out: ")
                   .append(trafficsOneSecond.stale())
                   .append(" Bytes")
                   .append("\n");
            builder.append("--Packets (1s)--")
                   .append("\n");
            builder.append("In: ")
                   .append(packetsOneSecond.newly())
                   .append("\n");
            builder.append("Out: ")
                   .append(packetsOneSecond.stale());

            event.getProxy()
                 .send(
                         new SendMessagePacket(
                                 packet.getType(),
                                 new TextMessageElement(builder.toString()).toMessage(),
                                 packet.getResponseId()
                         ),
                         result -> {
                         }
                 );
        }

        if (event.getPacket()
                 .getMessage()
                 .toPlainText()
                 .equals(".dummy_forward")) {
            List<ForwardMessage> messages = ApricotCollectionFactor.newArrayList();

            messages.add(new DummyForwardMessage(
                    packet.getSenderId(),
                    packet.getSender()
                          .getName(),
                    new TextMessageElement("草").toMessage()
            ));

            event.getProxy()
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
