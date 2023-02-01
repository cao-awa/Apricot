package com.github.cao.awa.apricot.message.element.cq.xml;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.util.message.*;

public class XmlMessageElement extends MessageElement {
    private final String data;
    private final int resId;

    public XmlMessageElement(String data) {
        this.data = data;
        this.resId = 0;
    }

    public XmlMessageElement(String data, int resId) {
        this.data = data;
        this.resId = resId;
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
