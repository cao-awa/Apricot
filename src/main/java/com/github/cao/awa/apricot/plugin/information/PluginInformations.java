package com.github.cao.awa.apricot.plugin.information;

import com.github.cao.awa.apricot.utils.collection.*;

import java.util.*;

public class PluginInformations {
    private final Map<String, String> informations = ApricotCollectionFactor.newHashMap();

    public static PluginInformations of() {
        return new PluginInformations();
    }

    public void add(String name, String information) {
        this.informations.put(
                name,
                information
        );
    }

    public Map<String, String> getInformations() {
        return informations;
    }
}
