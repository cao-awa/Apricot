package com.github.cao.awa.apricot.message.element.cq.xml;

import com.github.cao.awa.apricot.anntations.Stable;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.util.message.*;

@Stable
public class XmlMessageElement extends MessageElement {
    private final String data;
    private final int resId;

    private XmlMessageElement(String data) {
        this.data = data;
        this.resId = 0;
    }

    private XmlMessageElement(String data, int resId) {
        this.data = data;
        this.resId = resId;
    }

    public static XmlMessageElement xml(String data) {
        return new XmlMessageElement(data);
    }

    public static XmlMessageElement xml(String data, int resId) {
        return new XmlMessageElement(
                data,
                resId
        );
    }

    public int getResId() {
        return this.resId;
    }

    @Override
    public String toPlainText() {
        return "[CQ:xml,data=" + MessageUtil.escape(this.data) + ",resid=" + this.resId + "]";
    }

    @Override
    public String getShortName() {
        return "XmlMessageElement{data=" + this.data + ",resId=" + this.resId + "}";
    }

    @Override
    public boolean shouldIncinerate() {
        return true;
    }

    public String getData() {
        return this.data;
    }
}
