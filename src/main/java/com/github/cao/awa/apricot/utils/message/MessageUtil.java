package com.github.cao.awa.apricot.utils.message;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.server.*;
import it.unimi.dsi.fastutil.objects.*;

import java.util.*;

public class MessageUtil {
    public static Message process(ApricotServer server, String content) {
        int cursor = 0;
        int pos;
        List<MessageElement> elements = new ObjectArrayList<>();
        while ((pos = content.indexOf(
                "[CQ:",
                cursor
        )) != - 1) {
            // Process plain text.
            if (pos > cursor) {
                elements.add(new TextMessageElement(content.substring(
                        cursor,
                        pos
                )));
            }
            // Find cq code end.
            int endPos = content.indexOf(
                    "]",
                    pos
            );
            // Processing cq.
            String cq = content.substring(
                    pos + 1,
                    endPos
            );
            // Split the cq code.
            List<String> args = List.of(cq.split(","));

            // Deserialize and participate in the message.
            MessageElement element = server.createCq(args);
            elements.add(element);

            // Update cursors
            cursor = endPos + 2;
        }
        // If no cq code, mean this is fully plain text, process for all.
        if (elements.size() == 0) {
            elements.add(new TextMessageElement(content));
        }

        // Let all prepared element participate in message
        return new Message().participateAll(elements);
    }
}
