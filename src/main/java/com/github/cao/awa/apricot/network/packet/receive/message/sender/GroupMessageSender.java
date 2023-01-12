package com.github.cao.awa.apricot.network.packet.receive.message.sender;

import com.alibaba.fastjson2.*;

public class GroupMessageSender implements IgnoredIdMessageSender {
    private final int age;
    private final String name;
    private final String sex;
    private final long userId;
    private String area = "";
    private String level = "";
    private String role = "";
    private String card = "";
    private String title = "";

    public GroupMessageSender(int age, String name, String sex, long userId) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.userId = userId;
    }

    public GroupMessageSender(int age, String name, String sex, long userId, String area) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.userId = userId;
        this.area = area;
    }

    public GroupMessageSender(int age, String name, String sex, long userId, String area, String level) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.userId = userId;
        this.area = area;
        this.level = level;
    }

    public GroupMessageSender(int age, String name, String sex, long userId, String area, String level, String role) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.userId = userId;
        this.area = area;
        this.level = level;
        this.role = role;
    }

    public GroupMessageSender(int age, String name, String sex, long userId, String area, String level, String role, String card) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.userId = userId;
        this.area = area;
        this.level = level;
        this.role = role;
        this.card = card;
    }

    public GroupMessageSender(int age, String name, String sex, long userId, String area, String level, String role, String card, String title) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.userId = userId;
        this.area = area;
        this.level = level;
        this.role = role;
        this.card = card;
        this.title = title;
    }

    public static GroupMessageSender create(JSONObject json) {
        return new GroupMessageSender(
                json.getInteger("age"),
                json.getString("nickname"),
                json.getString("sex"),
                json.getLong("user_id"),
                json.getString("area"),
                json.getString("level"),
                json.getString("role"),
                json.getString("card"),
                json.getString("title")
        );
    }

    public String getArea() {
        return this.area;
    }

    public String getLevel() {
        return this.level;
    }

    public String getRole() {
        return this.role;
    }

    public String getCard() {
        return this.card;
    }

    public String getTitle() {
        return this.title;
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
