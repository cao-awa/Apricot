package com.github.cao.awa.apricot.plugin.ext.captcha.handler;

import com.alibaba.fastjson2.*;
import com.github.cao.awa.apricot.config.*;
import com.github.cao.awa.apricot.config.util.*;
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
import com.github.cao.awa.apricot.network.packet.send.group.mute.personal.normal.*;
import com.github.cao.awa.apricot.network.packet.send.message.*;
import com.github.cao.awa.apricot.network.router.*;
import com.github.cao.awa.apricot.plugin.ext.captcha.*;
import com.github.cao.awa.apricot.resource.loader.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.server.service.event.exclusive.*;
import com.github.cao.awa.apricot.util.io.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

public class JoinGroupCaptcha extends GroupMemberIncreasedEventHandler {
    private static final Random RANDOM = new Random();
    private final Configure configure = ConfigUtil.fromFile(
            "configs/plugins/ext/captcha/config.conf",
            () -> {
                return IOUtil.read(ResourcesLoader.get("plugins/captcha/config.conf"));
            }
    );

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

        boolean mute = this.configure.getBoolean("mute");

        EventTarget target = mute ? new EventTarget(
                - 1,
                packet.target()
                      .person(),
                packet.target()
                      .bot()
        ) : packet.target();

        CaptchaPlugin.CAPTCHA_CHECKER.answer(
                target,
                tester.result(),
                result -> {
                    proxy.send(new SendGroupMuteNormalPacket(
                            packet.getGroupId(),
                            packet.getUserId(),
                            0
                    ));
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
                    if (CaptchaPlugin.CAPTCHA_CHECKER.contains(trysToMuch.target())) {
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
                      target,
                      mute ? CaptchaPlugin.CAPTCHA_WITH_PRIVATE : CaptchaPlugin.CAPTCHA_WITH_GROUP,
                      - 1,
                      1000 * 60 * 5,
                      () -> {
                          proxy.send(new SendMessagePacket(
                                  MessageType.GROUP,
                                  new AssembledMessage().participate(new AtMessageElement(new AtTarget(
                                                                AtTargetType.PERSON,
                                                                packet.getUserId()
                                                        )))
                                                        .participate(new TextMessageElement("验证已超时！")),
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
                                              .participate(new TextMessageElement("请您在五分钟内回答此问题，直接发出得数：\n" + tester.getNumber1() + tester.getOperator() + tester.getNumber2() + "=?")),
                        packet.getGroupId()
                ),
                result -> {
                    // Message are not sent.
                    if (result.getMessageId() == 0) {
                        CaptchaPlugin.CAPTCHA_CHECKER.reset(target);
                    } else {
                        // Make passable for admin to cancel captcha
                        CaptchaPlugin.CAPTCHA_CHECKER.passable(
                                result.getMessageId(),
                                target
                        );
                    }
                }
        );

        if (this.configure.getBoolean("mute")) {
            proxy.send(new SendGroupMuteNormalPacket(
                    packet.getGroupId(),
                    packet.getUserId(),
                    60 * 5
            ));
        }
    }

    @Override
    public boolean accept(EventTarget target) {
        return EntrustEnvironment.get(
                () -> {
                    List<Long> list = JSONObject.parse(IOUtil.read(new FileInputStream("configs/plugins/internal/captcha/whitelist.json")))
                                                .getJSONArray("lists")
                                                .toList(Long.TYPE);
                    return target.group() != - 1 && list.contains(target.group());
                },
                false
        );
    }

    @Override
    public void reload() {
        this.configure.setDefault(
                "mute",
                false
        );
        this.configure.reload();
    }

    private static final class CalculateTester {
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
                case 1 -> new CalculateTester(
                        "-",
                        (x, y) -> x - y,
                        () -> RANDOM.nextInt(50)
                );
                case 2 -> new CalculateTester(
                        "x",
                        (x, y) -> x * y,
                        () -> RANDOM.nextInt(10)
                );
                default -> new CalculateTester(
                        "+",
                        Integer::sum,
                        () -> RANDOM.nextInt(50)
                );
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
