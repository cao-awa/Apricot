package com.github.cao.awa.apricot.network.packet.recevied.message.sender;

import com.alibaba.fastjson2.*;

public class PrivateMessageSender implements MessageSender {
    private final int age;
    private final String name;
    private final String sex;
    private final long userId;

    public PrivateMessageSender(int age, String name, String sex, long userId) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.userId = userId;
    }

    public static PrivateMessageSender create(JSONObject json) {
        return new PrivateMessageSender(
                json.getInteger("age"),
                json.getString("nickname"),
                json.getString("sex"),
                json.getLong("user_id")
        );
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

