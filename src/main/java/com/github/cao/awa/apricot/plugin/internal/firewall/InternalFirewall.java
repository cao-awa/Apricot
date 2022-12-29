package com.github.cao.awa.apricot.plugin.internal.firewall;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.plugin.firewall.*;

import java.util.*;

@AutoPlugin
public class InternalFirewall extends FirewallPlugin {
    private static final UUID ID = UUID.fromString("8e599f58-703f-4dc9-beb8-abee377fb39b");

    @Override
    public void onInitialize() {
        registerHandler(new InternalMessageFilter());
        registerHandler(new InternalGroupFilter());
        registerHandler(new InternalInvalidDataHandler());
    }

    @Override
    public UUID getUuid() {
        return ID;
    }

    @Override
    public String getName() {
        return "Internal Firewall";
    }
}
