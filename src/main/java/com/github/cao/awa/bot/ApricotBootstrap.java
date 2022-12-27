package com.github.cao.awa.bot;

import com.github.cao.awa.bot.plugin.internal.*;
import com.github.cao.awa.bot.server.*;

public class ApricotBootstrap {
    public static void main(String[] args) {
        new ApricotBootstrap().bootstrap();
    }

    public void bootstrap() {
        ApricotServer server = new ApricotServer();

        server.registerPlugin(new InternalPlugin());

        try {
            server.startup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
