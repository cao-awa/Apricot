package com.github.cao.awa.apricot.network.packet.receive.message.sender.anonymous;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;

public class AnonymousMessageSender implements IgnoredIdMessageSender {
    private final Anonymous anonymous;
    private final int age;
    private final String name;
    private final String sex;
    private final long userId;
    private String area = "";
    private String level = "";

    public AnonymousMessageSender(Anonymous anonymous, int age, String name, String sex, long userId) {
        this.anonymous = anonymous;
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.userId = userId;
    }

    public AnonymousMessageSender(Anonymous anonymous, int age, String name, String sex, long userId, String area) {
        this.anonymous = anonymous;
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.userId = userId;
        this.area = area;
    }

    public AnonymousMessageSender(Anonymous anonymous, int age, String name, String sex, long userId, String area, String level) {
        this.anonymous = anonymous;
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.userId = userId;
        this.area = area;
        this.level = level;
    }

    public static AnonymousMessageSender create(JSONObject anonymous, JSONObject sender) {
        return new AnonymousMessageSender(
                Anonymous.create(anonymous),
                sender.getInteger("age"),
                sender.getString("nickname"),
                sender.getString("sex"),
                sender.getLong("user_id"),
                sender.getString("area"),
                sender.getString("level")
        );
    }

    public Anonymous getAnonymous() {
        return this.anonymous;
    }

    public String getArea() {
        return this.area;
    }

    public String getLevel() {
        return this.level;
    }

    public int getAge() {
        return this.age;
    }

    public String getName() {
        return this.name;
    }

    public String getSex() {
        return this.sex;
    }

    public long getUserId() {
        return this.userId;
    }
}
