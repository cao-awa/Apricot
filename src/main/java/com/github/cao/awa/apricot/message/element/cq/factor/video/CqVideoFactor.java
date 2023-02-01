package com.github.cao.awa.apricot.message.element.cq.factor.video;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.factor.*;
import com.github.cao.awa.apricot.message.element.cq.video.*;
import com.github.cao.awa.apricot.server.*;

import java.util.*;

public class CqVideoFactor extends CqFactor {
    @Override
    public MessageElement create(ApricotServer server, Map<String, String> args) {
        return new VideoMessageElement(
                args.get("file"),
                args.get("cover")
        );
    }

    @Override
    public String getName() {
        return "CQ:video";
    }
}
