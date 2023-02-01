package com.github.cao.awa.apricot.message.forward.real;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.message.forward.*;

public class RealForwardMessage extends ForwardMessage {
    private final int messageId;

    public RealForwardMessage(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public void write(JSONObject json) {
        json.fluentPut(
                    "type",
                    "node"
            )
            .fluentPut(
                    "data",
                    new JSONObject().fluentPut(
                            "id",
                            this.messageId
                    )
            );
    }
}
