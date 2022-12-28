package com.github.cao.awa.apricot.network.packet.send.message;

import com.github.cao.awa.apricot.anntations.*;

public enum SendMessageType {
    PRIVATE("private"), GROUP("group"), @Unsupported GUILD("guild");

    private final String name;

    SendMessageType(String name) {
        this.name = name;
    }

    public static SendMessageType of(String name) {
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
