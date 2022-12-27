package com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function;

import java.io.*;

public interface ExceptingTemporary extends Serializable {
    void apply() throws Exception;
}
