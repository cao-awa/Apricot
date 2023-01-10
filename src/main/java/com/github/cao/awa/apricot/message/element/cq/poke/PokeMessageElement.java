package com.github.cao.awa.apricot.message.element.cq.poke;

import com.github.cao.awa.apricot.message.element.*;

public class PokeMessageElement extends MessageElement {
    private final long target;
    private final long causer;

    public PokeMessageElement(long target, long causer) {
        this.target = target;
        this.causer = causer;
    }

    @Override
    public String toPlainText() {
        return "[CQ:poke,qq=" + this.target + "]";
    }

    @Override
    public String getShortName() {
        return "PokeMessageElement{target=" + this.target + ",by=" + this.causer + "}";
    }

    @Override
    public boolean shouldIncinerate() {
        return true;
    }
}
