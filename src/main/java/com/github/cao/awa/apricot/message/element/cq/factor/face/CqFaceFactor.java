package com.github.cao.awa.apricot.message.element.cq.factor.face;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.face.*;
import com.github.cao.awa.apricot.message.element.cq.factor.*;
import com.github.cao.awa.apricot.server.*;

import java.util.*;

public class CqFaceFactor extends CqFactor {
    @Override
    public MessageElement create(ApricotServer server, Map<String, String> args) {
        return FaceMessageElement.face(
                Integer.parseInt(args.get("id"))
        );
    }

    @Override
    public String getName() {
        return "face";
    }
}
