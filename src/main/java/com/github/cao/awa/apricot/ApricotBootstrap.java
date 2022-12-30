package com.github.cao.awa.apricot;

import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.utils.times.*;

public class ApricotBootstrap {
    public static void main(String[] args) {
        new ApricotBootstrap().bootstrap();
    }

    public void bootstrap() {
        long start = TimeUtil.millions();
        ApricotServer server = new ApricotServer();

        try {
            server.startup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Apricot server startup done in " + TimeUtil.processMillion(start));
    }
}
