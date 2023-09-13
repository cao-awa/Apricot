package com.github.cao.awa.apricot.message.element.cq.image;

import com.github.cao.awa.apricot.anntations.Stable;
import com.github.cao.awa.apricot.message.element.*;

import java.io.*;

@Stable
public class ImageMessageElement extends MessageElement {
    private final File file;
    private final String url;
    private final String subType;

    private ImageMessageElement(File file, String url, String subType) {
        this.file = file;
        this.url = url;
        this.subType = subType;
    }

    public static ImageMessageElement image(String file, String url, String subType) {
        return new ImageMessageElement(new File(file), url, subType);
    }

    public static ImageMessageElement image(File file, String url, String subType) {
        return new ImageMessageElement(file, url, subType);
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
