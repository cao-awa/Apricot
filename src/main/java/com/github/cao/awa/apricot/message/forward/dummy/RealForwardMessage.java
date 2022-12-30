package com.github.cao.awa.apricot.message.forward.dummy;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.message.forward.*;

public class RealForwardMessage extends ForwardMessage {
    private final long messageId;

    public RealForwardMessage(long messageId) {
        this.messageId = messageId;
    }

    public long getMessageId() {
        return messageId;
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
