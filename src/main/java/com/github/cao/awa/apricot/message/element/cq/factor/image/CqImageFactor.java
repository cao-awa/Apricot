package com.github.cao.awa.apricot.message.element.cq.factor.image;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.element.image.*;
import com.github.cao.awa.apricot.message.element.cq.factor.*;
import com.github.cao.awa.apricot.server.*;

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
