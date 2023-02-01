package com.github.cao.awa.apricot.message.element.cq.video;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.util.message.*;

public class VideoMessageElement extends MessageElement {
    private final String file;
    private final String cover;

    public VideoMessageElement(String file) {
        this.file = file;
        this.cover = "";
    }

    public VideoMessageElement(String file, String cover) {
        this.file = file;
        this.cover = cover;
    }

    @Override
    public String toPlainText() {
        return "[CQ:video,file=" + MessageUtil.escape(this.file) + ",cover=" + this.cover + "]";
    }

    @Override
    public String getShortName() {
        return "RecordMessageElement{file=" + this.file + ",cover=" + this.cover + "}";
    }

    @Override
    public boolean shouldIncinerate() {
        return true;
    }

    public String getCover() {
        return this.cover;
    }

    public String getFile() {
        return this.file;
    }
}
