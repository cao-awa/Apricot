package com.github.cao.awa.apricot.message.cq.factor.replay;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.cq.element.replay.*;
import com.github.cao.awa.apricot.message.cq.factor.*;
import com.github.cao.awa.apricot.server.*;

import java.util.*;

public class CqReplyFactor extends CqFactor {
    @Override
    public MessageElement create(ApricotServer server, Map<String, String> args) {
        return new ReplyMessageElement(
                Long.parseLong(args.get("id"))
        );
    }

    @Override
    public String getName() {
        return "CQ:reply";
    }
}
