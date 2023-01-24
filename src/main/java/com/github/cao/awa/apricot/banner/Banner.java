package com.github.cao.awa.apricot.banner;

import com.github.cao.awa.apricot.util.text.*;

import java.util.*;
import java.util.function.*;

public class Banner {
    private final List<String> lines;

    public Banner(String banner) {
        this.lines = TextUtil.splitToList(banner, '\n');
    }

    public void forEach(Consumer<String> action) {
        this.lines.forEach(action);
    }

    public String get(int index) {
        return this.lines.get(index);
    }
}
