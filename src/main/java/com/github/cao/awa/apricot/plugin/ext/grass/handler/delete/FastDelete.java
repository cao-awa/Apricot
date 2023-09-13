package com.github.cao.awa.apricot.plugin.ext.grass.handler.delete;

import com.github.cao.awa.apricot.config.plugin.handler.*;
import com.github.cao.awa.apricot.event.handler.message.received.group.*;
import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.carve.*;
import com.github.cao.awa.apricot.message.element.cq.replay.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.network.packet.receive.message.group.received.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.GroupMessageSender;
import com.github.cao.awa.apricot.network.packet.receive.response.message.get.*;
import com.github.cao.awa.apricot.network.packet.send.group.mute.personal.normal.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.packet.send.recall.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.util.message.*;

import java.util.*;

public class FastDelete extends GroupMessageReceivedEventHandler {
    public static final String CONFIG_NAME = "FastDelete";
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

        ApsConfig config = config(CONFIG_NAME);

        if (MessageProcess.pureCommand(packet.getMessage(),
                                       "/d"
        )) {
            proxy.send(new SendMessagePacket(
                    packet.getType(),
                    AssembledMessage.of()
                                    .participate(ReplyMessageElement.reply(packet.getMessageId()))
                                    .participate(TextMessageElement.text(config.map("delete_responses")
                                                                               .getString("missing_reason"))),
                    packet.getResponseId()
            ));

            return;
        }

        if (MessageProcess.command(
                packet.getMessage(),
                "/d"
        )) {
            String plain = MessageProcess.plain(packet.getMessage());
            plain = plain.substring(plain.indexOf(" ") + 1);

            CarvedMessage<ReplyMessageElement> reply = packet.getMessage()
                                                             .carver(ReplyMessageElement.class);

            if (reply.size() == 1) {
                String response = config.map("delete_responses")
                                        .getString(plain);

                long targetId = reply.get(0)
                                     .getMessageId();

                if (response == null) {
                    proxy.send(new SendMessagePacket(
                            packet.getType(),
                            AssembledMessage.of()
                                            .participate(ReplyMessageElement.reply(packet.getMessageId()))
                                            .participate(TextMessageElement.text(config.map("delete_responses")
                                                                                       .getString("unexpected_error"))),
                            packet.getResponseId()
                    ));
                } else {
                    GetMessageResponsePacket messageResponsePacket = proxy.server()
                                                                          .getMessage(
                                                                                  proxy,
                                                                                  targetId
                                                                          );

                    long displayId = messageResponsePacket.getOwnId() == - 1 ?
                            messageResponsePacket.getMessageId() :
                            messageResponsePacket.getOwnId();


                    if (displayId != - 1) {
                        proxy.send(new SendRecallMessagePacket(targetId));
                        proxy.send(new SendMessagePacket(
                                packet.getType(),
                                AssembledMessage.of()
                                                .participate(ReplyMessageElement.reply(packet.getMessageId()))
                                                .participate(TextMessageElement.text(String.format(
                                                        config.map("delete_responses")
                                                              .getString("#"),
                                                        displayId
                                                ) + config.map("delete_responses")
                                                          .getString("deleted") + response)),
                                packet.getResponseId()
                        ));

                        proxy.send(new SendGroupMuteNormalPacket(
                                packet.getGroupId(),
                                messageResponsePacket.getSender()
                                                     .getSenderId(),
                                60 * 30
                        ));
                    } else {
                        proxy.send(new SendMessagePacket(
                                packet.getType(),
                                AssembledMessage.of()
                                                .participate(ReplyMessageElement.reply(packet.getMessageId()))
                                                .participate(TextMessageElement.text(String.format(
                                                        config.map("delete_responses")
                                                              .getString("unable_to_delete"),
                                                        targetId
                                                ))),
                                packet.getResponseId()
                        ));
                    }
                }
            } else {
                proxy.send(new SendMessagePacket(
                        packet.getType(),
                        AssembledMessage.of()
                                        .participate(ReplyMessageElement.reply(packet.getMessageId()))
                                        .participate(TextMessageElement.text(
                                                             config.map("delete_responses")
                                                                   .getString("fast_delete_must_reply_to_message")
                                                     )
                                        ),
                        packet.getResponseId()
                ));
            }
        }
    }
}
