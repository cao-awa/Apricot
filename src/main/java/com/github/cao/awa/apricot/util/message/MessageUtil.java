package com.github.cao.awa.apricot.util.message;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.cao.awa.apricot.util.text.*;

import java.util.*;

public class MessageUtil {
    public static AssembledMessage process(ApricotServer server, String content) {
        int cursor = 0;
        int pos;

        content = stripAndTrim(
                server,
                content
        );

        List<MessageElement> elements = ApricotCollectionFactor.newArrayList();
        while ((pos = content.indexOf(
                "[CQ:",
                cursor
        )) != - 1) {
            // Process plain text.
            if (pos > cursor) {
                String result = stripAndTrim(
                        server,
                        content.substring(
                                cursor,
                                pos
                        )
                );
                if (result.length() > 0) {
                    elements.add(new TextMessageElement(result));
                }
            }
            // Find cq code end.
            int endPos = content.indexOf(
                    "]",
                    pos + 4
            );
            if (endPos == - 1) {
                break;
            }
            // Processing cq.
            String cq = content.substring(
                    pos + 1,
                    endPos
            );

            // Split the cq code.
            List<String> args = TextUtil.splitToList(
                    cq,
                    ','
            );

            // Deserialize and participate in the message.
            MessageElement element = server.createCq(args);
            if (element != null) {
                elements.add(element);
            }

            // Update cursors
            cursor = endPos + 1;
        }

        // It means have CQ codes is unsupported
        if (elements.size() == 0) {
            elements.add(new TextMessageElement(content));
        } else {
            // It means have more information can be precessed to text element
            if (cursor < content.length()) {
                String source = content.substring(cursor);
                String result = stripAndTrim(
                        server,
                        source
                );
                elements.add(result.length() > 0 ? new TextMessageElement(result) : new TextMessageElement(source));
            }
        }

        // Let all prepared element participate in message
        return new AssembledMessage().participateAll(elements);
    }

    public static String stripAndTrim(ApricotServer server, String source) {
        return server.shouldCaverMessage() ?
               source.strip()
                     .trim() :
               source;
    }
}