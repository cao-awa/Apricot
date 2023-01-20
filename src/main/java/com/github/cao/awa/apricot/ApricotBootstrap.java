package com.github.cao.awa.apricot;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.thread.*;

@Stable
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
