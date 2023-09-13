package com.github.cao.awa.apricot.message.element.cq.factor.xml;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.factor.*;
import com.github.cao.awa.apricot.message.element.cq.xml.*;
import com.github.cao.awa.apricot.server.*;

import java.util.*;

public class CqXmlFactor extends CqFactor {
    @Override
    public MessageElement create(ApricotServer server, Map<String, String> args) {
        return args.containsKey("resid") ?
                XmlMessageElement.xml(
                        args.get("data"),
                        Integer.parseInt(args.get("resid"))
                ) :
                XmlMessageElement.xml(args.get("data"));
    }

    @Override
    public String getName() {
        return "xml";
    }
}
