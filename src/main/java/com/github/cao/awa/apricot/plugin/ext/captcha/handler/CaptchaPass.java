package com.github.cao.awa.apricot.plugin.ext.captcha.handler;

import com.github.cao.awa.apricot.event.handler.message.*;
import com.github.cao.awa.apricot.event.receive.message.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.cq.at.*;
import com.github.cao.awa.apricot.message.element.cq.at.target.*;
import com.github.cao.awa.apricot.message.element.cq.replay.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.network.packet.receive.message.*;
import com.github.cao.awa.apricot.network.packet.receive.message.sender.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import static com.github.cao.awa.apricot.plugin.ext.captcha.CaptchaPlugin.*;

public class CaptchaPass extends MessageEventHandler {
    /**
     * Process event.
     *
     * @param event event
     * @author cao_awa
     * @author 草二号机
     * @since 1.0.0
     */
    @Override
    public void onMessage(MessageEvent<?> event) {
        MessagePacket packet = event.getPacket();
        ApricotProxy proxy = event.proxy();
        ApricotServer server = proxy.server();

        if (packet.getType() == MessageType.GROUP) {
            Long messageId = EntrustEnvironment.trys(() -> packet.getMessage()
                                                                 .get(
                                                                         0,
                                                                         ReplyMessageElement.class
                                                                 )
                                                                 .getMessageId());

            if (messageId != null) {
                if (packet.getMessage()
                          .carver(TextMessageElement.class)
                          .toPlainText()
                          .equals("/pass")) {
                    if (CAPTCHA_CHECKER.isPassable(messageId)) {
                        if (packet.getSender() instanceof GroupMessageSender sender && (sender.getRole()
                                                                                              .equals("admin") || sender.getRole()
                                                                                                                        .equals("owner"))) {
                            CAPTCHA_CHECKER.pass(
                                    server,
                                    messageId
                            );

                            proxy.send(new SendMessagePacket(
                                    MessageType.GROUP,
                                    AssembledMessage.of()
                                                    .participate(ReplyMessageElement.reply(packet.getMessageId()))
                                                    .participate(AtMessageElement.at(AtTarget.of(
                                                            AtTargetType.PERSON,
                                                            packet.getSenderId()
                                                    )))
                                                    .participate(TextMessageElement.text("此验证已手动通过！")),
                                    packet.getResponseId()
                            ));
                        } else {
                            proxy.send(new SendMessagePacket(
                                    MessageType.GROUP,
                                    AssembledMessage.of()
                                                    .participate(ReplyMessageElement.reply(packet.getMessageId()))
                                                    .participate(AtMessageElement.at(AtTarget.of(
                                                            AtTargetType.PERSON,
                                                            packet.getSenderId()
                                                    )))
                                                    .participate(TextMessageElement.text("您没有权限处理Captcha验证！")),
                                    packet.getResponseId()
                            ));
                        }
                    }
                }
            }
        }
    }
}
