package com.github.cao.awa.apricot.message.element.cq.at;

import com.github.cao.awa.apricot.anntations.Stable;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.cq.at.target.*;

@Stable
public class AtMessageElement extends MessageElement {
    private final AtTarget target;

    private AtMessageElement(AtTarget target) {
        this.target = target;
    }

    public static AtMessageElement at(AtTarget target) {
        return new AtMessageElement(target);
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

