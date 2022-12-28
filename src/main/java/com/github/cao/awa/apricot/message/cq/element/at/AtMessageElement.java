package com.github.cao.awa.apricot.message.cq.element.at;

import com.github.cao.awa.apricot.message.*;

public class AtMessageElement extends MessageElement {
    private final long qq;

    public AtMessageElement(long qq) {
        this.qq = qq;
    }

    public long getQq() {
        return qq;
    }

    public String toString() {
        return "AtMessageElement{qq=" + this.qq + "}";
    }

    @Override
    public String toPlainText() {
        return "[CQ:at,qq=" + this.qq + "]";
    }

    @Override
    public String getShortName() {
        return "AtMessageElement{" + this.qq + "}";
    }
}

