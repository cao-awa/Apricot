package com.github.zhuaidadaya.rikaishinikui.handler.universal.affair;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.util.collection.*;

import java.util.*;

@Stable
public class Affair {
    private final List<Runnable> actions = ApricotCollectionFactor.linkedList();

    public static Affair of(Runnable action) {
        return new Affair().add(action);
    }

    public static Affair empty() {
        return new Affair();
    }

    public Affair add(Runnable action) {
        this.actions.add(action);
        return this;
    }

    public void done() {
        this.actions.forEach(Runnable::run);
    }
}
