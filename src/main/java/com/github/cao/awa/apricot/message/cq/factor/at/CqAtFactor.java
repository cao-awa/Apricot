package com.github.cao.awa.apricot.message.cq.factor.at;

import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.cq.element.at.*;
import com.github.cao.awa.apricot.message.cq.element.replay.*;
import com.github.cao.awa.apricot.message.cq.factor.*;
import com.github.cao.awa.apricot.server.*;

import java.util.*;

public class CqAtFactor extends CqFactor {
    @Override
    public MessageElement create(ApricotServer server, Map<String, String> args) {
        return new AtMessageElement(
                Long.parseLong(args.get("qq"))
        );
    }

    @Override
    public String getName() {
        return "CQ:at";
    }
}
