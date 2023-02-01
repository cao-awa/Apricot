package com.github.cao.awa.apricot.message.element;

import com.github.cao.awa.apricot.util.message.*;
import org.jetbrains.annotations.*;

public final class TextMessageElement extends MessageElement {
    private final @NotNull String text;

    public TextMessageElement(@NotNull String text) {
        this.text = text;
    }

    @NotNull
    public String getText() {
        return this.text;
    }

    public String toString() {
        return "TextMessageElement{text=" + this.text + "}";
    }

    @Override
    public String toPlainText() {
        return MessageUtil.escape(this.text);
    }

    @Override
    public String getShortName() {
        return "TextMessageElement{" + this.text + "}";
    }
}
