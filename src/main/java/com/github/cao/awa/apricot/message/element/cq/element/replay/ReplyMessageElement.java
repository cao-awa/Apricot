package com.github.cao.awa.apricot.message.element.cq.element.replay;

import com.github.cao.awa.apricot.message.element.*;

public class ReplyMessageElement extends MessageElement {
    private final int messageId;

    public ReplyMessageElement(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageId() {
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
