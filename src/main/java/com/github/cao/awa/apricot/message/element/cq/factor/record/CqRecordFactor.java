package com.github.cao.awa.apricot.message.element.cq.factor.record;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.factor.*;
import com.github.cao.awa.apricot.message.element.cq.record.*;
import com.github.cao.awa.apricot.server.*;

import java.util.*;

public class CqRecordFactor extends CqFactor {
    @Override
    public MessageElement create(ApricotServer server, Map<String, String> args) {
        return RecordMessageElement.record(
                args.get("file"),
                args.get("url"),
                Boolean.parseBoolean(args.get("magic"))
        );
    }

    @Override
    public String getName() {
        return "record";
    }
}
