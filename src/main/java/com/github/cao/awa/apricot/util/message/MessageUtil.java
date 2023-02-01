package com.github.cao.awa.apricot.util.message;

import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.cao.awa.apricot.util.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MessageUtil {
    public static AssembledMessage process(ApricotServer server, String content) {
        int cursor = 0;
        int pos;

        List<MessageElement> elements = ApricotCollectionFactor.newArrayList();
        while ((pos = content.indexOf(
                "[CQ:",
                cursor
        )) != - 1) {
            // Process plain text.
            if (pos > cursor) {
                String result = content.substring(
                        cursor,
                        pos
                );
                elements.add(new TextMessageElement(unescape(result)));
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
            elements.add(new TextMessageElement(unescape((content))));
        } else {
            // It means have more information can be precessed to text element
            if (cursor < content.length()) {
                String source = content.substring(cursor);
                String result = stripAndTrim(
                        server,
                        source
                );
                elements.add(result.length() > 0 ?
                             new TextMessageElement(unescape((result))) :
                             new TextMessageElement(unescape(source)));
            }
        }

        // Let all prepared element participate in message
        return new AssembledMessage(elements);
    }

    public static String stripAndTrim(ApricotServer server, String source) {
        return server.shouldCaverMessage() ?
               source.strip()
                     .trim() :
               source;
    }

    public static String unescape(@NotNull String source) {
        return source.replace(
                             "&amp;",
                             "&"
                     )
                     .replace(
                             "&#91;",
                             "["
                     )
                     .replace(
                             "&#93;",
                             "]"
                     )
                     .replace(
                             "&#44;",
                             ","
                     );
    }

    public static String unlw(@NotNull String source) {
        return source.replace(
                "\\n",
                "\n"
        );
    }

    public static String escape(@NotNull String source) {
        return source.replace(
                             "&",
                             "&amp;"
                     )
                     .replace(
                             "[",
                             "&#91;"

                     )
                     .replace(
                             "]",
                             "&#93;"

                     )
                     .replace(
                             ",",
                             "&#44;"

                     );
    }

    public static String lw(@NotNull String source) {
        return source.replace(
                "\n",
                "\\n"
        );
    }
}
