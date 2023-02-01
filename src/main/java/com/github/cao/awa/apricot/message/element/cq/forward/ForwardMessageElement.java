package com.github.cao.awa.apricot.message.element.cq.forward;

import com.github.cao.awa.apricot.message.element.*;

public class ForwardMessageElement extends MessageElement {
    private final String id;

    public ForwardMessageElement(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toPlainText() {
        return "[CQ:forward,id=" + this.id + "]";
    }

    @Override
    public String getShortName() {
        return "ForwardMessageElement{" + this.id + "}";
    }

    @Override
    public boolean shouldIncinerate() {
        return true;
    }

    public String toString() {
        return "ForwardMessageElement{id=" + this.id + "}";
    }
}
