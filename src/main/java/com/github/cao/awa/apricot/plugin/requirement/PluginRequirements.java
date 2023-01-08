package com.github.cao.awa.apricot.plugin.requirement;

import com.github.cao.awa.apricot.util.collection.*;

import java.util.*;

public class PluginRequirements {
    private final Set<PluginRequirement> requirements = ApricotCollectionFactor.newHashSet();

    public PluginRequirements(PluginRequirement... requirements) {
        for (PluginRequirement requirement : requirements) {
            add(requirement);
        }
    }

    public static PluginRequirements of(PluginRequirement... requirements) {
        return new PluginRequirements(requirements);
    }

    public PluginRequirements add(PluginRequirement requirement) {
        this.requirements.add(requirement);
        return this;
    }
}
