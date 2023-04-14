package com.github.cao.awa.apricot.util.message;

import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.MessageElement;
import com.github.cao.awa.apricot.message.element.cq.at.*;
import com.github.cao.awa.apricot.message.element.cq.replay.ReplyMessageElement;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.network.packet.receive.message.MessagePacket;
import com.github.cao.awa.apricot.server.ApricotServer;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

public class MessageProcess {
    public static boolean isAt(AssembledMessage message, long userId) {
        return message.size() == 1 && EntrustEnvironment.get(
                () -> message.get(
                                     0,
                                     AtMessageElement.class
                             )
                             .getTarget()
                             .getAtPerson() == userId,
                false
        );
    }

    public static boolean afterAt(AssembledMessage message, long userId) {
        return EntrustEnvironment.get(
                () -> message.get(
                                     0,
                                     AtMessageElement.class
                             )
                             .getTarget()
                             .getAtPerson() == userId,
                false
        );
    }

    public static boolean startWith(AssembledMessage message, Class<? extends MessageElement> type) {
        return EntrustEnvironment.get(
                () -> type.isInstance(message.get(
                        0,
                        type
                )),
                false
        );
    }

    public static boolean isReply(ApricotServer server, AssembledMessage message, long target) {
        return EntrustEnvironment.get(
                () -> server.getMessagesHeadOffice()
                            .getFromId(message.get(
                                                      0,
                                                      ReplyMessageElement.class
                                              )
                                              .getMessageId()
                            )
                            .getSenderId() == target,
                false
        );
    }

    public static boolean hasAt(AssembledMessage message, long userId) {
        return EntrustEnvironment.get(
                () -> message.carver(AtMessageElement.class)
                             .get(0)
                             .getTarget()
                             .getAtPerson() == userId,
                false
        );
    }

    public static boolean command(AssembledMessage message, String prefix) {
        return message.carver(TextMessageElement.class)
                      .toPlainText()
                      .startsWith(prefix);
    }

    public static boolean command(MessagePacket message, String prefix) {
        return message.getMessage()
                      .carver(TextMessageElement.class)
                      .toPlainText()
                      .startsWith(prefix);
    }

    public static String plain(AssembledMessage message) {
        return message.carver(TextMessageElement.class)
                      .toPlainText();
    }

    public static String plain(MessagePacket packet) {
        return plain(packet.getMessage());
    }
}
