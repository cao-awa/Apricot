package com.github.cao.awa.apricot.message.element.cq.factor.json;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.factor.*;
import com.github.cao.awa.apricot.message.element.cq.json.*;
import com.github.cao.awa.apricot.server.*;

import java.util.*;

public class CqJsonFactor extends CqFactor {
    @Override
    public MessageElement create(ApricotServer server, Map<String, String> args) {
        return args.containsKey("resid") ? new JsonMessageElement(
                JSONObject.parse(args.get("data")),
                Integer.parseInt(args.get("resid"))
        ) : new JsonMessageElement(JSONObject.parse(args.get("data")));
    }

    @Override
    public String getName() {
        return "json";
    }
}
