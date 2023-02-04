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
import com.github.zhuaidadaya.rikaishinikui.handler.universal.pair.*;

import java.util.*;
import java.util.function.*;

public class CalCaptcha extends GroupMessageReceivedEventHandler {
    private final Map<EventTarget, Pair<Object, Consumer<GroupMessageReceivedPacket>>> answers = ApricotCollectionFactor.newConcurrentHashMap();

    public void answer(EventTarget target, Object answer, Consumer<GroupMessageReceivedPacket> callback) {
        this.answers.put(
                target,
                Pair.of(
                        answer,
                        callback
                )
        );
    }

    public void reset(EventTarget target) {
        this.answers.remove(target);
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
                                  .toPlainText();

        Pair<Object, Consumer<GroupMessageReceivedPacket>> answer = this.answers.get(packet.target());

        if (answer != null) {
            if (answer.left() == null) {
                return;
            }
            if (answer.left()
                      .toString()
                      .equals(answerText)) {
                answer.right()
                      .accept(packet);
            } else {
                proxy.send(new SendRecallMessagePacket(packet.getMessageId()));
            }
        }
    }
}
