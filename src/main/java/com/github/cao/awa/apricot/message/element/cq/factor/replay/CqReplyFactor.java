package com.github.cao.awa.apricot.message.element.cq.factor.replay;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.factor.*;
import com.github.cao.awa.apricot.message.element.cq.replay.*;
import com.github.cao.awa.apricot.server.*;

import java.util.*;

public class CqReplyFactor extends CqFactor {
    @Override
    public MessageElement create(ApricotServer server, Map<String, String> args) {
        return ReplyMessageElement.reply(
                Long.parseLong(args.get("id"))
        );
    }

    @Override
    public String getName() {
        return "reply";
    }
}
