package com.github.cao.awa.bot.message.cq.factor;

import com.github.cao.awa.bot.message.*;
import com.github.cao.awa.bot.network.packet.*;
import com.github.cao.awa.bot.server.*;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CqDeserializer {
    private final Map<String, CqFactor> factors = new Object2ObjectOpenHashMap<>();

    @Nullable
    public MessageElement deserializer(ApricotServer server, List<String> elements) {
        String name = elements.get(0);
        if (this.factors.containsKey(name)) {
            Map<String, String> args = new Object2ObjectOpenHashMap<>();

            for (int i = 1;elements.size() > i;i++) {
                String content = elements.get(i);
                int delimiter = content.indexOf("=");
                args.put(content.substring(0, delimiter), content.substring(delimiter + 1));
            }

            return this.factors.get(name)
                               .create(server, args);
        }
        return null;
    }

    public void register(CqFactor factor) {
        this.factors.put(factor.getName(), factor);
    }
}
