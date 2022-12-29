package com.github.cao.awa.apricot.message.cq.element.at.target;

public class AtTarget {
    private final AtTargetType type;
    private final long atPerson;

    public AtTarget(AtTargetType type) {
        this.type = type;
        this.atPerson = - 1;
    }

    public AtTarget(AtTargetType type, long atPerson) {
        this.type = type;
        this.atPerson = atPerson;
    }

    public static AtTarget of(String source) {
        AtTargetType type = source.equalsIgnoreCase("all") ? AtTargetType.ALL : AtTargetType.PERSON;
        return new AtTarget(
                type,
                type == AtTargetType.PERSON ? Long.parseLong(source) : - 1
        );
    }

    public AtTargetType getType() {
        return this.type;
    }

    public long getAtPerson() {
        return this.atPerson;
    }

    public String toString() {
        return this.type == AtTargetType.ALL ? "all" : String.valueOf(this.atPerson);
    }
}
