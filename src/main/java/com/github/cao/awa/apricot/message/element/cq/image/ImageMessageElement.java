package com.github.cao.awa.apricot.message.element.cq.image;

import com.github.cao.awa.apricot.message.element.*;

import java.io.*;

public class ImageMessageElement extends MessageElement {
    private final File file;
    private final String url;
    private final String subType;

    public ImageMessageElement(String file, String url, String subType) {
        this.file = new File(file);
        this.url = url;
        this.subType = subType;
    }

    public ImageMessageElement(File file, String url, String subType) {
        this.file = file;
        this.url = url;
        this.subType = subType;
    }

    public File getFile() {
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
