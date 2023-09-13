package com.github.cao.awa.apricot.plugin.ext.grass.handler.status;

import com.github.cao.awa.apricot.event.handler.message.received.group.*;
import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import com.github.cao.awa.apricot.http.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.message.forward.*;
import com.github.cao.awa.apricot.message.forward.dummy.*;
import com.github.cao.awa.apricot.network.packet.receive.message.group.received.*;
import com.github.cao.awa.apricot.network.packet.send.forward.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.cao.awa.apricot.util.message.*;
import com.github.cao.awa.apricot.util.time.*;

import java.util.*;

public class BotStatus extends GroupMessageReceivedEventHandler {
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
    public void onMessageReceived(GroupMessageReceivedEvent<?> event) {
        GroupMessageReceivedPacket packet = event.getPacket();
        ApricotProxy proxy = event.proxy();

        // Do not response commands.
        if (MessageProcess.command(
                packet.getMessage(),
                "..status"
        )) {
            if (MessageProcess.hasAt(
                    packet.getMessage(),
                    packet.getBotId()
            )) {
                long start = TimeUtil.millions();
                int baiduCode = ApricotHttpUtil.get("https://www.baidu.com/")
                                               .response()
                                               .code();
                long baiduLatency = TimeUtil.processMillion(start);

                start = TimeUtil.millions();
                int googleCode = ApricotHttpUtil.get("https://www.google.com/")
                                                .response()
                                                .code();
                long googleLatency = TimeUtil.processMillion(start);

                List<ForwardMessage> messages = ApricotCollectionFactor.newArrayList();

                messages.add(new DummyForwardMessage(
                        packet.getSenderId(),
                        packet.getSender()
                              .getName(),
                        new AssembledMessage().participate(new TextMessageElement("..."))
                ));

                proxy.send(new SendMessagesForwardPacket(
                        packet.getType(),
                        messages,
                        packet.getResponseId()
                ));
            }
        }
    }

    public String getCodeName(int code) {
        return switch (code) {
            case 200 -> "OK";
            default -> "Unknown";
        };
    }

    @Override
    public void onException(Exception exception) {
        exception.printStackTrace();
    }
}
