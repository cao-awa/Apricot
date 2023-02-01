package com.github.cao.awa.apricot.message.element;

import com.github.cao.awa.apricot.message.assemble.*;

public abstract class MessageElement {
    public abstract String toPlainText();

    public abstract String getShortName();

    public AssembledMessage toMessage() {
        return new AssembledMessage().participate(this);
    }

    public boolean shouldIncinerate() {
        return false;
    }
}
