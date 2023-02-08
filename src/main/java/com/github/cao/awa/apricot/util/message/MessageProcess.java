package com.github.cao.awa.apricot.util.message;

import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.cq.at.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
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

    public static String plain(AssembledMessage message) {
        return message.carver(TextMessageElement.class)
                      .toPlainText();
    }
}
