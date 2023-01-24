package com.github.zhuaidadaya.rikaishinikui.handler.universal.pair;

import com.github.cao.awa.apricot.anntations.*;

@Stable
public record Pair<T, Y>(T left, Y right) {
}
