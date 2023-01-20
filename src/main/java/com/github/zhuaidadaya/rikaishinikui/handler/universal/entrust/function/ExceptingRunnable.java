package com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function;

import com.github.cao.awa.apricot.anntations.*;

import java.io.*;

@Stable
public interface ExceptingRunnable extends Serializable {
    void apply() throws Exception;
}
