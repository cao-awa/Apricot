package com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.model.ana;

import com.github.cao.awa.apricot.util.text.TextUtil;

import java.util.List;

public class PreAnalysis  {
    public List<String> parseStr(String temp) {
        return TextUtil.splitToList(temp, ' ');
    }
}
