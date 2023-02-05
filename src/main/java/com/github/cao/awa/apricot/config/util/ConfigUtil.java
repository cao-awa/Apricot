package com.github.cao.awa.apricot.config.util;

import com.github.cao.awa.apricot.config.*;
import com.github.cao.awa.apricot.util.io.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.*;

import java.io.*;

public class ConfigUtil {
    public static Configure fromFile(String file, ExceptingSupplier<String> writeAbsent) {
        Configure configs = new Configure(() -> "");
        configs.init(() -> EntrustEnvironment.receptacle(receptacle -> {
            try {
                File config = new File(file);
                if (! config.isFile()) {
                    EntrustEnvironment.trys(() -> config.getParentFile()
                                                        .mkdirs());

                    receptacle.set(writeAbsent.get());

                    EntrustEnvironment.operation(
                            new BufferedWriter(new FileWriter(config)),
                            writer -> IOUtil.write(
                                    writer,
                                    receptacle.get()
                            )
                    );
                }

                if (receptacle.get() == null) {
                    receptacle.set(IOUtil.read(new BufferedReader(new FileReader(config))));
                }
            } catch (Exception e) {
                receptacle.set("");
            }
        }));
        return configs;
    }
}
