package com.github.cao.awa.apricot.message.element.cq.json;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.anntations.Stable;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.util.message.*;

@Stable
public class JsonMessageElement extends MessageElement {
    private final JSONObject data;
    private final int resId;

    private JsonMessageElement(JSONObject data) {
        this.data = data;
        this.resId = 0;
    }

    private JsonMessageElement(JSONObject data, int resId) {
        this.data = data;
        this.resId = resId;
    }

    public static JsonMessageElement json(JSONObject data) {
        return new JsonMessageElement(data);
    }

    public static JsonMessageElement json(JSONObject data, int resId) {
        return new JsonMessageElement(data, resId);
    }

    public int getResId() {
        return this.resId;
    }

    @Override
    public String toPlainText() {
        return "[CQ:json,data=" + MessageUtil.escape(this.data.toString()) + ",resid=" + this.resId + "]";
    }

    @Override
    public String getShortName() {
        return "JsonMessageElement{data=" + this.data.toString() + ",resId=" + this.resId + "}";
    }

    @Override
    public boolean shouldIncinerate() {
        return true;
    }

    public String getData() {
        return MessageUtil.escape(this.data.toString());
    }
}
