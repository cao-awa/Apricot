package com.github.cao.awa.apricot.message.element.cq.element.at;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.element.at.target.*;

public class AtMessageElement extends MessageElement {
    private final AtTarget target;

    public AtMessageElement(AtTarget target) {
        this.target = target;
    }

    public AtTarget getTarget() {
        return this.target;
    }

    public String toString() {
        return "AtMessageElement{qq=" + this.target + "}";
    }

    @Override
    public String toPlainText() {
        return "[CQ:at,qq=" + this.target + "]";
    }

    @Override
    public String getShortName() {
        return "AtMessageElement{" + this.target + "}";
    }
}

