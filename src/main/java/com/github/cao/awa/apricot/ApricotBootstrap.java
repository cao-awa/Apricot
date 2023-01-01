package com.github.cao.awa.apricot;

import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.thread.*;

public class ApricotBootstrap {
    public static void main(String[] args) {
        new ApricotBootstrap().bootstrap();
    }

    public void bootstrap() {
        ThreadUtil.setName("Apricot");
        ApricotServer server = new ApricotServer();

        try {
            server.startup();
        } catch (Exception e) {
            server.shutdown();
        }
    }
}
