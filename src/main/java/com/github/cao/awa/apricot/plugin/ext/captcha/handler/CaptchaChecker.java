package com.github.cao.awa.apricot.plugin.ext.captcha.handler;

import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.option.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.pair.*;

import java.util.*;
import java.util.function.*;

public class CaptchaChecker {
    private final Map<EventTarget, Pair<Object, BiOption<Consumer<MessageReceivedPacket>>>> answers = ApricotCollectionFactor.concurrentHashMap();
    private final Map<EventTarget, Integer> trys = ApricotCollectionFactor.concurrentHashMap();

    private final Map<Long, EventTarget> passable = ApricotCollectionFactor.concurrentHashMap();
    private final Map<EventTarget, Long> passable_ = ApricotCollectionFactor.concurrentHashMap();

    public static void release(ApricotServer server, MessageReceivedPacket packet) {
        server.getEventManager()
              .release(packet.target());
    }

    public void answer(EventTarget target, Object answer, Consumer<MessageReceivedPacket> callback) {
        this.answers.put(
                target,
                Pair.of(
                        answer,
                        BiOption.of(
                                callback,
                                r -> {
                                }
                        )
                )
        );
        this.trys.put(
                target,
                0
        );
    }

    public Pair<Object, BiOption<Consumer<MessageReceivedPacket>>> get(EventTarget target) {
        return this.answers.get(target);
    }

    public void answer(EventTarget target, Object answer, Consumer<MessageReceivedPacket> callback, Consumer<MessageReceivedPacket> trysToMuchCallback) {
        this.answers.put(
                target,
                Pair.of(
                        answer,
                        BiOption.of(
                                callback,
                                trysToMuchCallback
                        )
                )
        );
        this.trys.put(
                target,
                0
        );
    }

    public void passable(long messageId, EventTarget target) {
        this.passable.put(
                messageId,
                target
        );
        this.passable_.put(
                target,
                messageId
        );
    }

    public void trys(EventTarget target) {
        this.trys.computeIfPresent(
                target,
                (k, v) -> v + 1
        );
    }

    public int tried(EventTarget target) {
        return this.trys.getOrDefault(
                target,
                - 1
        );
    }

    public void pass(ApricotServer server, long messageId) {
        if (isPassable(messageId)) {
            release(
                    server,
                    this.passable.get(messageId)
            );
            reset(this.passable.get(messageId));
        }
    }

    public boolean isPassable(long messageId) {
        return this.passable.get(messageId) != null;
    }

    private static void release(ApricotServer server, EventTarget target) {
        server.getEventManager()
              .release(target);
    }

    public void reset(EventTarget target) {
        this.answers.remove(target);
        this.trys.remove(target);
        long messageId = this.passable_.getOrDefault(
                target,
                - 1L
        );
        this.passable.remove(messageId);
        this.passable_.remove(target);
    }

    public boolean contains(EventTarget target) {
        return this.answers.containsKey(target);
    }
}