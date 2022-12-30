package com.github.cao.awa.apricot.plugin.internal.plugin;

import com.github.cao.awa.apricot.event.handler.accomplish.message.*;
import com.github.cao.awa.apricot.event.receive.accomplish.message.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.forward.*;
import com.github.cao.awa.apricot.message.forward.dummy.*;
import com.github.cao.awa.apricot.network.handler.*;
import com.github.cao.awa.apricot.network.packet.recevied.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.packet.send.message.forward.*;
import com.github.cao.awa.apricot.network.packet.send.message.recall.*;
import com.github.cao.awa.apricot.network.packet.send.name.card.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.server.service.counter.traffic.*;
import com.github.cao.awa.apricot.utils.collection.*;
import com.github.cao.awa.apricot.utils.times.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle.*;
import org.apache.logging.log4j.*;

import java.util.*;

public class InternalGroupHandler extends MessageReceivedEventHandler {
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

//        if (packet.getMessage().toPlainText().startsWith("[CQ:forward")) {
            LOGGER.info("GROUP * " + packet.getSender()
                                           .getName() + ": " + packet.getMessage()
                                                                     .toPlainText());
//        }

        if (event.getPacket()
                 .getMessage()
                 .toPlainText()
                 .equals("test_recall")) {
            event.getProxy()
                 .send(
                         new SendRecallMessagePacket(event.getPacket()
                                                          .getMessageId()),
                         result -> {
                             System.out.println(result.getIdentifier());
                             System.out.println(result.getResponse());
                         }
                 );
        }

        if (event.getPacket()
                 .getMessage()
                 .toPlainText()
                 .equals(".traffic")) {

            ApricotServer server = proxy.getServer();

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
            builder.append("--Traffic (10s)--")
                   .append("\n");
            builder.append("In: ")
                   .append(trafficsOneSecond.newly())
                   .append(" Bytes")
                   .append("\n");
            builder.append("Out: ")
                   .append(trafficsOneSecond.stale())
                   .append(" Bytes")
                   .append("\n");
            builder.append("--Packets (10s)--")
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
                                 packet.getResponseId(),
                                 packet.getResponseId()
                         ),
                         result -> {
                             System.out.println(result.getIdentifier());
                             System.out.println(result.getResponse());
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
                    new TextMessageElement("草了").toMessage()
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
                             System.out.println(result.getIdentifier());
                             System.out.println(result.getResponse());
                         }
                 );
        }

        if (event.getPacket().getMessage().toPlainText().equals(".change_name")) {
            proxy.send(new SendSetGroupNameCardPacket(String.valueOf(TimeUtil.millions()), packet.getResponseId(), packet.getSenderId()));
        }
    }
}
