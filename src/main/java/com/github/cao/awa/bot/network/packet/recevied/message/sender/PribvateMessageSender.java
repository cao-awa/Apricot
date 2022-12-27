package com.github.cao.awa.bot.network.packet.recevied.message.sender;

import com.alibaba.fastjson2.*;

public class PribvateMessageSender implements MessageSender {
    private final int age;
    private final String name;
    private final String sex;
    private final long userId;

    public PribvateMessageSender(int age, String name, String sex, long userId) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.userId = userId;
    }

    public static PribvateMessageSender create(JSONObject json) {
        return new PribvateMessageSender(
                json.getInteger("age"),
                json.getString("nickname"),
                json.getString("sex"),
                json.getLong("user_id")
        );
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public long getUserId() {
        return userId;
    }
}

