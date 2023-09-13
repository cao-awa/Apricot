package com.github.cao.awa.apricot.message.element.cq.poke;

import com.github.cao.awa.apricot.anntations.Stable;
import com.github.cao.awa.apricot.message.element.*;

@Stable
public class PokeMessageElement extends MessageElement {
    private final long target;

    private PokeMessageElement(long target) {
        this.target = target;
    }

    public static PokeMessageElement poke(long target) {
        return new PokeMessageElement(target);
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
