package com.github.cao.awa.apricot.plugin.internal.captcha.handler;

import com.github.cao.awa.apricot.event.handler.member.change.increase.*;
import com.github.cao.awa.apricot.event.receive.member.change.increase.*;
import com.github.cao.awa.apricot.event.target.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.assemble.*;
import com.github.cao.awa.apricot.message.element.cq.at.*;
import com.github.cao.awa.apricot.message.element.cq.at.target.*;
import com.github.cao.awa.apricot.message.element.cq.replay.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.network.packet.receive.member.change.increase.*;
import com.github.cao.awa.apricot.network.packet.send.group.kick.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.server.service.event.exclusive.*;

import java.util.*;

import static com.github.cao.awa.apricot.plugin.internal.captcha.CaptchaPlugin.*;

public class JoinGroupCaptcha extends GroupMemberIncreasedEventHandler {
    private static final Random random = new Random();

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
    public void onIncrease(GroupMemberIncreasedEvent<?> event) {
        GroupMemberIncreasedPacket packet = event.getPacket();
        ApricotProxy proxy = event.getProxy();
        ApricotServer server = proxy.server();

        int num1 = random.nextInt(
                0,
                30
        );
        int num2 = random.nextInt(
                0,
                30
        );

        proxy.send(new SendMessagePacket(
                MessageType.GROUP,
                new AssembledMessage().participate(new AtMessageElement(new AtTarget(
                                              AtTargetType.PERSON,
                                              packet.getUserId()
                                      )))
                                      .participate(new TextMessageElement("为了验证您是否为自动加群，请在五分钟内回答此问题：" + num1 + "+" + num2 + "=?")),
                packet.getGroupId()
        ));

        CAPTCHA_CHECKER.answer(
                packet.target(),
                num1 + num2,
                result -> {
                    server.getEventManager()
                          .release(result.target());
                    CAPTCHA_CHECKER.reset(result.target());
                    proxy.send(new SendMessagePacket(
                            MessageType.GROUP,
                            new AssembledMessage().participate(new ReplyMessageElement(result.getMessageId()))
                                                  .participate(new AtMessageElement(new AtTarget(
                                                          AtTargetType.PERSON,
                                                          packet.getUserId()
                                                  )))
                                                  .participate(new TextMessageElement("已完成Captcha验证，欢迎您的加入！")),
                            packet.getGroupId()
                    ));
                }
        );

        server.getEventManager()
              .exclusive(
                      packet.target(),
                      CAPTCHA_CHECKER,
                      - 1,
                      1000 * 60 * 5,
                      () -> {
                          proxy.send(new SendGroupKickPacket(
                                  packet.getGroupId(),
                                  packet.getUserId(),
                                  false
                          ));
                      },
                      EventExclusiveTarget.ALL
              );
    }

    @Override
    public boolean accept(EventTarget target) {
        // Default not enabled.
        return false;
    }
}
