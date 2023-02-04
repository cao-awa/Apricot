package com.github.cao.awa.apricot.server.service.event.exclusive;

import com.github.cao.awa.apricot.event.handler.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.util.time.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle.*;

public record EventExclusive(EventHandler<?> handler, Receptacle<Integer> counts, long recorded, long timeout,
                             Runnable timeoutCallback, EventExclusiveTarget target) {
    public EventExclusive(EventHandler<?> handler, Receptacle<Integer> counts) {
        this(
                handler,
                counts,
                - 1,
                - 1,
                () -> {
                },
                EventExclusiveTarget.SELF
        );
    }

    public boolean isValid() {
        return this.recorded == - 1 || TimeUtil.processMillion(this.recorded) < this.timeout;
    }

    public boolean blocked(Plugin plugin) {
        return this.target.getLevel() == EventExclusiveLevel.ALL || (this.target.getLevel() == EventExclusiveLevel.SPECIALS && this.target.getTargets()
                                                                                                                                          .contains(plugin)) || this.target.getLevel() == EventExclusiveLevel.SELF && this.handler.getPlugin() == plugin;
    }
}
