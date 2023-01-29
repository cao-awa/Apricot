package com.github.cao.awa.apricot.message.element.cq.face;

import com.github.cao.awa.apricot.message.element.*;

public class FaceMessageElement extends MessageElement {
    private final int id;

    public FaceMessageElement(int id) {
        this.id = id;
    }

    @Override
    public String toPlainText() {
        return "[CQ:face,id=" + this.id + "]";
    }

    @Override
    public String getShortName() {
        return "FaceMessageElement{" + this.id + "}";
    }

    public String toString() {
        return "FaceMessageElement{id=" + this.id + "}";
    }
}
