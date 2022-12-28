package com.github.cao.awa.apricot;

import com.github.cao.awa.apricot.plugin.internal.*;
import com.github.cao.awa.apricot.server.*;

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
