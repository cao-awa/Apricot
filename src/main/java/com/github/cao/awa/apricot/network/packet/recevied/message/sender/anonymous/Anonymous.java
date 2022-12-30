package com.github.cao.awa.apricot.network.packet.recevied.message.sender.anonymous;

import com.alibaba.fastjson2.*;

public class Anonymous {
    private final String name;
    private final String flag;
    private final long id;

    public Anonymous(String name, String flag, long id) {
        this.name = name;
        this.flag = flag;
        this.id = id;
    }

    public static Anonymous create(JSONObject anonymous) {
        return new Anonymous(
                anonymous.getString("name"),
                anonymous.getString("flag"),
                anonymous.getLong("id")
        );
    }

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

    public long getId() {
        return id;
    }
}
