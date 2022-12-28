package com.github.cao.awa.apricot.message;

public final class TextMessageElement extends MessageElement {
    private final String text;

    public TextMessageElement(String text) {
        this.text = text;
    }

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
}
