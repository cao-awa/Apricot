package com.github.cao.awa.bot.message.cq.factor;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.bot.message.*;
import com.github.cao.awa.bot.network.packet.*;
import com.github.cao.awa.bot.server.*;

import java.util.*;

public abstract class CqFactor {
    public abstract MessageElement create(ApricotServer server, Map<String, String> args);
    public abstract String getName();
}
