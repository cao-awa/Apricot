package com.github.cao.awa.apricot.plugin.internal.captcha.handler;

import com.github.cao.awa.apricot.event.handler.member.change.decrease.*;
import com.github.cao.awa.apricot.event.receive.member.change.decrease.*;

import static com.github.cao.awa.apricot.plugin.internal.captcha.CaptchaPlugin.*;

public class LeaveGroupCaptcha extends GroupMemberDecreasedEventHandler {
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
    public void onDecrease(GroupMemberDecreasedEvent<?> event) {
        CAPTCHA_CHECKER.reset(event.getPacket()
                                   .target());
        event.getProxy()
             .server()
             .getEventManager()
             .release(event.getPacket()
                           .target());
    }
}
