package com.github.cao.awa.apricot.message.element.cq.image;

import com.github.cao.awa.apricot.message.element.*;

public class ImageMessageElement extends MessageElement {
    private final String file;
    private final String url;
    private final String subType;

    public ImageMessageElement(String file, String url, String subType) {
        this.file = file;
        this.url = url;
        this.subType = subType;
    }

    public String getFile() {
        return this.file;
    }

    public String getUrl() {
        return this.url;
    }

    public String getSubType() {
        return this.subType;
    }

    public String toString() {
        return "ImageMessageElement{file=" + this.file + ", url=" + this.url + ", subtype=" + this.subType + "}";
    }

    @Override
    public String toPlainText() {
        return "[CQ:image,file=" + this.file + ",url=" + this.url + (this.subType == null ?
                                                                     "" :
                                                                     ",subType=" + this.subType) + "]";
    }

    @Override
    public String getShortName() {
        return "ImageMessageElement{" + this.url + "}";
    }
}
