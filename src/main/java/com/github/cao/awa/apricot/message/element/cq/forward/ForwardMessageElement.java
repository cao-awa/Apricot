package com.github.cao.awa.apricot.message.element.cq.forward;

import com.github.cao.awa.apricot.anntations.Stable;
import com.github.cao.awa.apricot.message.element.*;

@Stable
public class ForwardMessageElement extends MessageElement {
    private final String id;

    private ForwardMessageElement(String id) {
        this.id = id;
    }

    public static ForwardMessageElement forward(String id) {
        return new ForwardMessageElement(id);
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
