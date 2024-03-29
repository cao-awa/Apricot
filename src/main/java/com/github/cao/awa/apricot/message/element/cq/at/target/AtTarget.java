package com.github.cao.awa.apricot.message.element.cq.at.target;

public class AtTarget {
    private final AtTargetType type;
    private final long atPerson;
    private final String title;

    private AtTarget(AtTargetType type) {
        this.type = type;
        this.atPerson = - 1;
        this.title = null;
    }

    private AtTarget(AtTargetType type, String title) {
        this.type = type;
        this.atPerson = - 1;
        this.title = title;
    }


    private AtTarget(AtTargetType type, long atPerson, String title) {
        this.type = type;
        this.atPerson = atPerson;
        this.title = title;
    }

    private AtTarget(AtTargetType type, long atPerson) {
        this.type = type;
        this.atPerson = atPerson;
        this.title = null;
    }

    public static AtTarget of(String source) {
        AtTargetType type = source.equalsIgnoreCase("all") ? AtTargetType.ALL : AtTargetType.PERSON;
        return new AtTarget(
                type,
                type == AtTargetType.PERSON ? Long.parseLong(source) : - 1
        );
    }

    public static AtTarget of(AtTargetType type) {
        return new AtTarget(type);
    }

    public static AtTarget of(AtTargetType type, String title) {
        return new AtTarget(type,
                            title
        );
    }


    public static AtTarget of(AtTargetType type, long atPerson, String title) {
        return new AtTarget(type,
                            atPerson,
                            title
        );
    }

    public static AtTarget of(AtTargetType type, long atPerson) {
        return new AtTarget(type,
                            atPerson
        );
    }

    public String getTitle() {
        return title;
    }

    public AtTargetType getType() {
        return this.type;
    }

    public long getAtPerson() {
        return this.atPerson;
    }

    public String toString() {
        return this.type == AtTargetType.ALL ?
                "all" :
                this.title == null ? String.valueOf(this.atPerson) : this.atPerson + ",name=" + this.title;
    }
}
