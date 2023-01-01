package com.github.cao.awa.apricot.approval.group;

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
