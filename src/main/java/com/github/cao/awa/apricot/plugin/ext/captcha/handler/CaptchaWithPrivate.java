package com.github.cao.awa.apricot.plugin.ext.captcha.handler;

import com.github.cao.awa.apricot.event.handler.message.received.personal.*;
import com.github.cao.awa.apricot.event.receive.message.personal.received.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.personal.received.*;
import com.github.cao.awa.apricot.network.packet.send.recall.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.option.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.pair.*;

import java.util.function.*;

import static com.github.cao.awa.apricot.plugin.ext.captcha.CaptchaPlugin.*;

public class CaptchaWithPrivate extends PrivateMessageReceivedEventHandler {
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
    public void onMessageReceived(PrivateMessageReceivedEvent<?> event) {

    }

    @Override
    public void onExclusive(PrivateMessageReceivedEvent<?> event) {
        final PrivateMessageReceivedPacket packet = event.getPacket();
        final ApricotProxy proxy = event.proxy();
        ApricotServer server = proxy.server();

        String answerText = packet.getMessage()
                                  .carver(TextMessageElement.class)
                                  .toPlainText()
                                  .strip()
                                  .trim();

        Pair<Object, BiOption<Consumer<MessageReceivedPacket>>> answer = CAPTCHA_CHECKER.get(packet.target());

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
                CaptchaChecker.release(
                        server,
                        packet
                );
                CAPTCHA_CHECKER.reset(packet.target());
            } else {
                proxy.send(new SendRecallMessagePacket(packet.getMessageId()));

                CAPTCHA_CHECKER.trys(packet.target());
            }
            if (CAPTCHA_CHECKER.tried(packet.target()) > 4) {
                answer.right()
                      .second()
                      .accept(packet);
                CaptchaChecker.release(
                        server,
                        packet
                );
            }
        }
    }
}
