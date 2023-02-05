package com.github.cao.awa.apricot.message.element.cq.factor.at;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.at.*;
import com.github.cao.awa.apricot.message.element.cq.at.target.*;
import com.github.cao.awa.apricot.message.element.cq.factor.*;
import com.github.cao.awa.apricot.server.*;

import java.util.*;

public class CqAtFactor extends CqFactor {
    @Override
    public MessageElement create(ApricotServer server, Map<String, String> args) {
        return new AtMessageElement(AtTarget.of(args.get("qq")));
    }

    @Override
    public String getName() {
        return "at";
    }
}
