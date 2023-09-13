package com.github.cao.awa.apricot.message.element.plain.text;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.message.element.*;
import org.jetbrains.annotations.*;

@Stable
public final class TextMessageElement extends MessageElement {
    private final @NotNull String text;

    private TextMessageElement(@NotNull String text) {
        this.text = text;
    }

    public static TextMessageElement text(String text) {
        return new TextMessageElement(text);
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
        return this.text;
    }

    @Override
    public String getShortName() {
        return "TextMessageElement{" + this.text + "}";
    }
}
