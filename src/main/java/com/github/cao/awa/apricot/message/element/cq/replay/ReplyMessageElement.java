package com.github.cao.awa.apricot.message.element.cq.replay;

import com.github.cao.awa.apricot.message.element.*;

public class ReplyMessageElement extends MessageElement {
    private final long messageId;

    public ReplyMessageElement(long messageId) {
        this.messageId = messageId;
    }

    public long getMessageId() {
        return this.messageId;
    }

    public String toString() {
        return "ReplyMessageElement{id=" + this.messageId + "}";
    }

    @Override
    public String toPlainText() {
        return "[CQ:reply,id=" + this.messageId + "]";
    }

    @Override
    public String getShortName() {
        return "ReplyMessageElement{" + this.messageId + "}";
    }
}
