package com.github.cao.awa.apricot.event.target;

import com.github.cao.awa.apricot.anntations.*;

@Stable
public record EventTarget(long group, long person, long bot) {
    public static final EventTarget INVALID = new EventTarget(
            - 1,
            - 1,
            - 1
    );
}
