package com.github.cao.awa.apricot.plugin.internal.firewall;

import com.github.cao.awa.apricot.plugin.firewall.*;

import java.util.*;

public class InternalFirewall  extends FirewallPlugin {
    private static final UUID ID = UUID.fromString("8e599f58-703f-4dc9-beb8-abee377fb39b");

    public InternalFirewall() {
        registerHandler(new InternalMessageFilter());
        registerHandler(new InternalGroupFilter());
    }

    @Override
    public UUID getUuid() {
        return ID;
    }
}
