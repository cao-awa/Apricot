package com.github.cao.awa.apricot.plugin.ext.grass.handler.mute;

import com.github.cao.awa.apricot.event.handler.message.received.group.*;
import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.cq.replay.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.network.packet.receive.message.group.received.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;
import com.github.cao.awa.apricot.network.packet.send.group.mute.personal.normal.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.message.*;

import java.util.*;

public class MuteMe extends GroupMessageReceivedEventHandler {
    public static final String CONFIG_NAME = "MuteMe";
    private static final Random RANDOM = new Random();

    /**
     * Process event.
     *
     * @param event event
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
                "禁言我"
        )) {
            if (MessageProcess.hasAt(
                    packet.getMessage(),
                    packet.getBotId()
            )) {
                if (packet.getSender() instanceof GroupMessageSender sender && (sender.getRole()
                                                                                      .equals("admin") || sender.getRole()
                                                                                                                .equals("owner"))) {
                    proxy.send(new SendMessagePacket(
                            packet.getType(),
                            AssembledMessage.of()
                                            .participate(ReplyMessageElement.reply(packet.getMessageSeq()))
                                            .participate(TextMessageElement.text(config(CONFIG_NAME).str("cannot_mute_response"))),
                            packet.getResponseId()
                    ));
                } else {
                    proxy.send(new SendMessagePacket(
                            packet.getType(),
                            AssembledMessage.of()
                                            .participate(ReplyMessageElement.reply(packet.getMessageSeq()))
                                            .participate(TextMessageElement.text(config(CONFIG_NAME).str("do_mute_me_response"))),
                            packet.getResponseId()
                    ));

                    proxy.send(new SendGroupMuteNormalPacket(
                            packet.getGroupId(),
                            packet.getSenderId(),
                            RANDOM.nextInt(
                                    30,
                                    3 * 60
                            )
                    ));
                }
            }
        }
    }
}
