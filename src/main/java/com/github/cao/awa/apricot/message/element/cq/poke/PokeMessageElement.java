package com.github.cao.awa.apricot.message.element.cq.poke;

import com.github.cao.awa.apricot.message.element.*;

public class PokeMessageElement extends MessageElement {
    private final long target;

    public PokeMessageElement(long target) {
        this.target = target;
    }

    @Override
    public String toPlainText() {
        return "[CQ:poke,qq=" + this.target + "]";
    }

    @Override
    public String getShortName() {
        return "PokeMessageElement{target=" + this.target + "}";
    }

    @Override
    public boolean shouldIncinerate() {
        return true;
    }

    public long getTarget() {
        return this.target;
    }
}
