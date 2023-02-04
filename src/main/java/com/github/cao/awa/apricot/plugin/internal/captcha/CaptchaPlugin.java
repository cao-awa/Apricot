package com.github.cao.awa.apricot.plugin.internal.captcha;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.plugin.internal.captcha.handler.*;
import org.jetbrains.annotations.*;

@AutoPlugin
public class CaptchaPlugin extends Plugin {
    public static final CaptchaChecker CAPTCHA_CHECKER = new CaptchaChecker();
    public static final CaptchaPass CAPTCHA_PASS = new CaptchaPass();

    @Override
    public @NotNull String getName() {
        return "Captcha";
    }

    @Override
    public void onInitialize() {
        registerHandler(new JoinGroupCaptcha());
        registerHandler(new LeaveGroupCaptcha());
        registerHandler(CAPTCHA_CHECKER);
        registerHandler(CAPTCHA_PASS);
    }

    @Override
    public String version() {
        return "0.0.1";
    }
}
