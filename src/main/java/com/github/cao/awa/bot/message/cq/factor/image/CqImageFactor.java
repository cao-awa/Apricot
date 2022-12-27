package com.github.cao.awa.bot.message.cq.factor.image;

import com.github.cao.awa.bot.message.*;
import com.github.cao.awa.bot.message.cq.factor.*;
import com.github.cao.awa.bot.message.cq.image.*;
import com.github.cao.awa.bot.server.*;

import java.util.*;

public class CqImageFactor extends CqFactor {
    @Override
    public MessageElement create(ApricotServer server, Map<String, String> args) {
        return new ImageMessageElement(
                args.get("file"),
                args.get("url"),
                args.get("subType")
        );
    }

    @Override
    public String getName() {
        return "CQ:image";
    }
}
