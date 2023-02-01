package com.github.cao.awa.apricot.message.element.cq.factor.forward;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.factor.*;
import com.github.cao.awa.apricot.message.element.cq.forward.*;
import com.github.cao.awa.apricot.server.*;

import java.util.*;

public class CqForwardFactor extends CqFactor {
    @Override
    public MessageElement create(ApricotServer server, Map<String, String> args) {
        return new ForwardMessageElement(
                args.get("id")
        );
    }

    @Override
    public String getName() {
        return "CQ:forward";
    }
}
