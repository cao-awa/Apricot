package com.github.cao.awa.apricot.message.forward.dummy;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.forward.*;

public class DummyForwardMessage extends ForwardMessage {
    private final long userId;
    private final String userName;
    private final AssembledMessage message;

    public DummyForwardMessage(long userId, String userName, AssembledMessage message) {
        this.userId = userId;
        this.userName = userName;
        this.message = message;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public AssembledMessage getMessage() {
        return message;
    }

    public void write(JSONObject json) {
        json.fluentPut(
                    "type",
                    "node"
            )
            .fluentPut(
                    "data",
                    new JSONObject().fluentPut(
                                            "name",
                                            this.userName
                                    )
                                    .fluentPut(
                                            "uin",
                                            this.userId
                                    )
                                    .fluentPut(
                                            "content",
                                            this.message.toPlainText()
                                    )
            );
    }
}
