package com.github.cao.awa.apricot.message.element.cq.video;

import com.github.cao.awa.apricot.anntations.Stable;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.util.message.*;

@Stable
public class VideoMessageElement extends MessageElement {
    private final String file;
    private final String cover;

    private VideoMessageElement(String file) {
        this.file = file;
        this.cover = "";
    }

    private VideoMessageElement(String file, String cover) {
        this.file = file;
        this.cover = cover;
    }

    public static VideoMessageElement video(String file) {
        return new VideoMessageElement(file);
    }

    public static VideoMessageElement video(String file, String cover) {
        return new VideoMessageElement(
                file,
                cover
        );
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
