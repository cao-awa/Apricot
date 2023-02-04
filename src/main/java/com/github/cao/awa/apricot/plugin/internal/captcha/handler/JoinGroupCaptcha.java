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
import java.util.function.*;

import static com.github.cao.awa.apricot.plugin.internal.captcha.CaptchaPlugin.*;

public class JoinGroupCaptcha extends GroupMemberIncreasedEventHandler {
    private static final Random RANDOM = new Random();

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

        CalculateTester tester = CalculateTester.select();

        CAPTCHA_CHECKER.answer(
                packet.target(),
                tester.result(),
                result -> {
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
                },
                trysToMuch -> {
                    if (CAPTCHA_CHECKER.contains(trysToMuch.target())) {
                        proxy.send(new SendMessagePacket(
                                MessageType.GROUP,
                                new AssembledMessage().participate(new ReplyMessageElement(trysToMuch.getMessageId()))
                                                      .participate(new AtMessageElement(new AtTarget(
                                                              AtTargetType.PERSON,
                                                              packet.getUserId()
                                                      )))
                                                      .participate(new TextMessageElement("尝试次数过多，此验证已失效！")),
                                packet.getGroupId()
                        ));
                        proxy.send(new SendGroupKickPacket(
                                packet.getGroupId(),
                                packet.getUserId(),
                                false
                        ));
                    }
                }
        );

        server.getEventManager()
              .exclusive(
                      packet.target(),
                      CAPTCHA_CHECKER,
                      - 1,
                      1000 * 60 * 5,
                      () -> {
                          proxy.send(new SendMessagePacket(
                                  MessageType.GROUP,
                                  new AssembledMessage().participate(new AtMessageElement(new AtTarget(
                                                                AtTargetType.PERSON,
                                                                packet.getUserId()
                                                        )))
                                                        .participate(new TextMessageElement("验证已超时")),
                                  packet.getGroupId()
                          ));
                          proxy.send(new SendGroupKickPacket(
                                  packet.getGroupId(),
                                  packet.getUserId(),
                                  false
                          ));
                      },
                      EventExclusiveTarget.ALL
              );

        proxy.send(
                new SendMessagePacket(
                        MessageType.GROUP,
                        new AssembledMessage().participate(new AtMessageElement(new AtTarget(
                                                      AtTargetType.PERSON,
                                                      packet.getUserId()
                                              )))
                                              .participate(new TextMessageElement("为了验证您是否为自动加群，请在五分钟内回答此问题：" + tester.getNumber1() + tester.getOperator() + tester.getNumber2() + "=?")),
                        packet.getGroupId()
                ),
                result -> {
                    CAPTCHA_CHECKER.passable(
                            result.getMessageId(),
                            packet.target()
                    );
                }
        );
    }

    @Override
    public boolean accept(EventTarget target) {
        // Default not enabled.
        return target.group() == 326503646;
    }

    private static final class CalculateTester {
        private static final CalculateTester ADD = new CalculateTester(
                "+",
                Integer::sum,
                () -> RANDOM.nextInt(50)
        );
        private static final CalculateTester SUBTRACT = new CalculateTester(
                "-",
                (x, y) -> x - y,
                () -> RANDOM.nextInt(50)
        );
        private static final CalculateTester MULTIPLY = new CalculateTester(
                "x",
                (x, y) -> x * y,
                () -> RANDOM.nextInt(10)
        );

        private final String operator;
        private final int result;
        private final int number1;
        private final int number2;

        CalculateTester(String operator, BiFunction<Integer, Integer, Integer> function, Supplier<Integer> generator) {
            this.operator = operator;
            this.number1 = generator.get();
            this.number2 = generator.get();
            this.result = function.apply(
                    this.number1,
                    this.number2
            );
        }

        public static CalculateTester select() {
            return switch (RANDOM.nextInt(4)) {
                case 1 -> SUBTRACT;
                case 2 -> MULTIPLY;
                default -> ADD;
            };
        }

        public int result() {
            return this.result;
        }

        public String getOperator() {
            return this.operator;
        }

        public int getNumber1() {
            return this.number1;
        }

        public int getNumber2() {
            return this.number2;
        }
    }
}
