package com.github.cao.awa.apricot.target;

public record EventTarget(long group, long person, long bot) {
    public static final EventTarget INVALID = new EventTarget(
            - 1,
            - 1,
            - 1
    );
}
