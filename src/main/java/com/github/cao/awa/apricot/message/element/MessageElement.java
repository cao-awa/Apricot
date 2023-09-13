package com.github.cao.awa.apricot.message.element;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.message.assemble.*;

@Stable
public abstract class MessageElement {
    public abstract String toPlainText();

    public abstract String getShortName();

    public AssembledMessage toMessage() {
        return AssembledMessage.of()
                               .participate(this);
    }

    public boolean shouldIncinerate() {
        return false;
    }
}
