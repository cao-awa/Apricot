package com.github.cao.awa.apricot.server.service.event.exclusive;

import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.util.collection.*;

import java.util.*;

public class EventExclusiveTarget {
    public static final EventExclusiveTarget SELF = new EventExclusiveTarget(EventExclusiveLevel.SELF);
    public static final EventExclusiveTarget ALL = new EventExclusiveTarget(EventExclusiveLevel.ALL);

    private final EventExclusiveLevel level;
    private final List<Plugin> targets = ApricotCollectionFactor.newArrayList();

    public List<Plugin> getTargets() {
        return this.targets;
    }

    public EventExclusiveTarget(EventExclusiveLevel level) {
        this.level = level;
    }

    public EventExclusiveTarget(EventExclusiveLevel level, Plugin targets) {
        this.level = level;
        this.targets.add(targets);
    }

    public EventExclusiveTarget(EventExclusiveLevel level, List<Plugin> targets) {
        this.level = level;
        this.targets.addAll(targets);
    }

    public EventExclusiveLevel getLevel() {
        return this.level;
    }
}
