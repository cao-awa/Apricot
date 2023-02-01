package com.github.cao.awa.apricot.approval.group;

import com.github.cao.awa.apricot.anntations.*;

@Stable
public enum ApprovalType {
    ADD("add"), INVITE("invite");

    private final String name;

    ApprovalType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
