package com.github.cao.awa.apricot.message.element.cq.face;

import com.github.cao.awa.apricot.anntations.Stable;
import com.github.cao.awa.apricot.message.element.*;

@Stable
public class FaceMessageElement extends MessageElement {
    private final int id;

    private FaceMessageElement(int id) {
        this.id = id;
    }

    public static FaceMessageElement face(int id) {
        return new FaceMessageElement(id);
    }

    public int getId() {
        return this.id;
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
