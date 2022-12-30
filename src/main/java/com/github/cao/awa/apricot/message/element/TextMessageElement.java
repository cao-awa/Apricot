package com.github.cao.awa.apricot.message.element;

import org.jetbrains.annotations.*;

public final class TextMessageElement extends MessageElement {
    private final @Nullable String text;

    public TextMessageElement(@Nullable String text) {
        this.text = text;
    }

    public @Nullable String getText() {
        return this.text;
    }

    public String toString() {
        return "TextMessageElement{text=" + this.text + "}";
    }

    @Override
    public String toPlainText() {
        return this.text;
    }

    @Override
    public String getShortName() {
        return "TextMessageElement{" + this.text + "}";
    }
}
