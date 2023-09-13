package com.github.cao.awa.apricot.message.element.cq.replay;

import com.github.cao.awa.apricot.anntations.Stable;
import com.github.cao.awa.apricot.message.element.*;

@Stable
public class ReplyMessageElement extends MessageElement {
    private final long messageSeq;

    private ReplyMessageElement(long messageSeq) {
        this.messageSeq = messageSeq;
    }

    public static ReplyMessageElement reply(long messageSeq) {
        return new ReplyMessageElement(messageSeq);
    }

    public long getMessageSeq() {
        return this.messageSeq;
    }

    public String toString() {
        return "ReplyMessageElement{seq=" + this.messageSeq + "}";
    }

    @Override
    public String toPlainText() {
        return "[CQ:reply,seq=" + this.messageSeq + "]";
    }

    @Override
    public String getShortName() {
        return "ReplyMessageElement{" + this.messageSeq + "}";
    }
}
