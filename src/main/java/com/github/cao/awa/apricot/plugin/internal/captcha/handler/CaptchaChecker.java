package com.github.cao.awa.apricot.plugin.internal.captcha.handler;

import com.github.cao.awa.apricot.event.handler.message.received.group.*;
import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.network.packet.receive.message.group.received.*;
import com.github.cao.awa.apricot.network.packet.send.recall.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.option.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.pair.*;

import java.util.*;
import java.util.function.*;

public class CaptchaChecker extends GroupMessageReceivedEventHandler {
    private final Map<EventTarget, Pair<Object, BiOption<Consumer<GroupMessageReceivedPacket>>>> answers = ApricotCollectionFactor.newConcurrentHashMap();
    private final Map<EventTarget, Integer> trys = ApricotCollectionFactor.newConcurrentHashMap();

    private final Map<Long, EventTarget> passable = ApricotCollectionFactor.newConcurrentHashMap();
    private final Map<EventTarget, Long> passable_ = ApricotCollectionFactor.newConcurrentHashMap();

    public void answer(EventTarget target, Object answer, Consumer<GroupMessageReceivedPacket> callback) {
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

    public void answer(EventTarget target, Object answer, Consumer<GroupMessageReceivedPacket> callback, Consumer<GroupMessageReceivedPacket> trysToMuchCallback) {
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

    public void pass(ApricotServer server, long messageId) {
        if (isPassable(messageId)) {
            release(
                    server,
                    this.passable.get(messageId)
            );
            reset(this.passable.get(messageId));
        }
    }

    public boolean contains(EventTarget target) {
        return this.answers.containsKey(target);
    }

    public boolean isPassable(long messageId) {
        return this.passable.get(messageId) != null;
    }

    /**
     * Process event.
     *
     * @param event
     *         event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void onMessageReceived(GroupMessageReceivedEvent<?> event) {

    }

    @Override
    public void onExclusive(GroupMessageReceivedEvent<?> event) {
        final GroupMessageReceivedPacket packet = event.getPacket();
        final ApricotProxy proxy = event.getProxy();
        ApricotServer server = proxy.server();

        String answerText = packet.getMessage()
                                  .carver(TextMessageElement.class)
                                  .toPlainText()
                                  .strip()
                                  .trim();

        Pair<Object, BiOption<Consumer<GroupMessageReceivedPacket>>> answer = this.answers.get(packet.target());

        if (answer != null) {
            if (answer.left() == null) {
                return;
            }
            if (answer.left()
                      .toString()
                      .equals(answerText)) {
                answer.right()
                      .first()
                      .accept(packet);
                release(
                        server,
                        packet
                );
                reset(packet.target());
            } else {
                proxy.send(new SendRecallMessagePacket(packet.getMessageId()));

                this.trys.computeIfPresent(
                        packet.target(),
                        (k, v) -> v + 1
                );
            }
            if (this.trys.getOrDefault(
                    packet.target(),
                    - 1
            ) > 2) {
                answer.right()
                      .second()
                      .accept(packet);
                release(
                        server,
                        packet
                );
            }
        }
    }

    private static void release(ApricotServer server, GroupMessageReceivedPacket packet) {
        server.getEventManager()
              .release(packet.target());
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
}
