package com.github.cao.awa.apricot.message.element.cq.factor.poke;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.element.replay.*;
import com.github.cao.awa.apricot.message.element.cq.factor.*;
import com.github.cao.awa.apricot.server.*;

import java.util.*;

public class CqPokeFactor extends CqFactor {
    @Override
    public MessageElement create(ApricotServer server, Map<String, String> args) {
        return new ReplyMessageElement(
                Integer.parseInt(args.get("qq"))
        );
    }

    @Override
    public String getName() {
        return "CQ:poke";
    }
}
