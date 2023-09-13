package com.github.cao.awa.apricot.message.element.cq.record;

import com.github.cao.awa.apricot.anntations.Stable;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.util.message.*;

@Stable
public class RecordMessageElement extends MessageElement {
    private final String file;
    private final String url;
    private final boolean magic;

    private RecordMessageElement(String file) {
        this(
                file,
                ""
        );
    }

    private RecordMessageElement(String file, String url) {
        this(
                file,
                url,
                false
        );
    }

    private RecordMessageElement(String file, String url, boolean magic) {
        this.file = file;
        this.url = url;
        this.magic = magic;
    }

    public static RecordMessageElement record(String file) {
        return new RecordMessageElement(file);
    }

    public static RecordMessageElement record(String file, String url) {
        return new RecordMessageElement(
                file,
                url
        );
    }

    public static RecordMessageElement record(String file, String url, boolean magic) {
        return new RecordMessageElement(
                file,
                url,
                magic
        );
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
