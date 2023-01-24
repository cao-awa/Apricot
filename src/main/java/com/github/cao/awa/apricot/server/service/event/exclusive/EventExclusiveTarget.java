package com.github.cao.awa.apricot.server.service.event.exclusive;

import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.util.collection.*;

import java.util.*;

public class EventExclusiveTarget {
    public static final EventExclusiveTarget SELF = new EventExclusiveTarget(EventExclusiveLevel.SELF);
    public static final EventExclusiveTarget ALL = new EventExclusiveTarget(EventExclusiveLevel.ALL);

    private final EventExclusiveLevel level;
    private final List<Plugin> target = ApricotCollectionFactor.newArrayList();

    public List<Plugin> getTargets() {
        return this.target;
    }

    public EventExclusiveTarget(EventExclusiveLevel level) {
        this.level = level;
    }

    public EventExclusiveTarget(EventExclusiveLevel level, Plugin target) {
        this.level = level;
        this.target.add(target);
    }

    public EventExclusiveTarget(EventExclusiveLevel level, List<Plugin> targets) {
        this.level = level;
        this.target.addAll(targets);
    }

    public EventExclusiveLevel getLevel() {
        return this.level;
    }
}
