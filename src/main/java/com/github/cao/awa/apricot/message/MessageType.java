package com.github.cao.awa.apricot.message;

import com.github.cao.awa.apricot.anntations.*;

@Stable
public enum MessageType {
    PRIVATE("private"), GROUP("group"), @Unsupported GUILD("guild");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public static MessageType of(String name) {
        return switch (name.toLowerCase()) {
            case "private" -> PRIVATE;
            case "group" -> GROUP;
            case "guild" -> GUILD;
            default -> throw new IllegalArgumentException("Unable to find type: '" + name + "'");
        };
    }

    public String getName() {
        return this.name;
    }
}
