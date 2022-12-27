package com.github.cao.awa.bot.util;

public class StringLinedBuilder {
    private final StringBuilder builder = new StringBuilder();
    private final String lineWrap;

    public StringLinedBuilder(String lineWrap) {
        this.lineWrap = lineWrap;
    }

    public void append(String str) {
        builder.append(str).append(lineWrap);
    }


    public void append(String str, Object... args) {
        append(str.formatted(args));
    }

    public String toString() {
        return builder.toString();
    }
}
