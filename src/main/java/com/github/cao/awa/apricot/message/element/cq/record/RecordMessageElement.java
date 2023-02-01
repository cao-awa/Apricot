package com.github.cao.awa.apricot.message.element.cq.record;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.util.message.*;

public class RecordMessageElement extends MessageElement {
    private final String file;
    private final String url;
    private final boolean magic;

    public RecordMessageElement(String file) {
        this(
                file,
                ""
        );
    }

    public RecordMessageElement(String file, String url) {
        this(
                file,
                url,
                false
        );
    }

    public RecordMessageElement(String file, String url, boolean magic) {
        this.file = file;
        this.url = url;
        this.magic = magic;
    }

    public boolean isMagic() {
        return this.magic;
    }

    public String getUrl() {
        return this.url;
    }

    @Override
    public String toPlainText() {
        return "[CQ:record,file=" + MessageUtil.escape(this.file) + ",url=" + this.url + ",magic=" + this.magic + "]";
    }

    @Override
    public String getShortName() {
        return "RecordMessageElement{file=" + this.file + ",url=" + this.url + ",magic=" + this.magic + "}";
    }

    @Override
    public boolean shouldIncinerate() {
        return true;
    }

    public String getFile() {
        return this.file;
    }
}
