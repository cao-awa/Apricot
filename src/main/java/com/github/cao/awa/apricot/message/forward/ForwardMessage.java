package com.github.cao.awa.apricot.message.forward;

import com.alibaba.fastjson2.*;

public abstract class ForwardMessage {
    public abstract void write(JSONObject json);
}
