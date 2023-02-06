package com.github.cao.awa.apricot.plugin.ext.captcha;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.plugin.ext.captcha.handler.*;
import com.github.cao.awa.apricot.plugin.name.*;
import org.jetbrains.annotations.*;

@AutoPlugin
public class CaptchaPlugin extends Plugin {
    public static final CaptchaWithPrivate CAPTCHA_WITH_PRIVATE = new CaptchaWithPrivate();
    public static final CaptchaWithGroup CAPTCHA_WITH_GROUP = new CaptchaWithGroup();
    public static final CaptchaChecker CAPTCHA_CHECKER = new CaptchaChecker();
    public static final CaptchaPass CAPTCHA_PASS = new CaptchaPass();

    @Override
    public @NotNull PluginName name() {
        return PluginName.of(
                "Captcha",
                "Captcha"
        );
    }

    @Override
    public void onInitialize() {
        registerHandler(new JoinGroupCaptcha());
        registerHandler(new LeaveGroupCaptcha());
        registerHandler(CAPTCHA_WITH_GROUP);
        registerHandler(CAPTCHA_WITH_PRIVATE);
        registerHandler(CAPTCHA_PASS);
    }

    @Override
    public String version() {
        return "1.0.2";
    }
}
