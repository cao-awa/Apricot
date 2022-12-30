package com.github.cao.awa.apricot.message.element.cq.factor;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.server.*;

import java.util.*;

public abstract class CqFactor {
    public abstract MessageElement create(ApricotServer server, Map<String, String> args);
    public abstract String getName();
}
