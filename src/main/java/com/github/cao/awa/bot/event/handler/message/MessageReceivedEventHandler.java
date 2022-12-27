package com.github.cao.awa.bot.event.handler.message;

import com.github.cao.awa.bot.event.handler.*;
import com.github.cao.awa.bot.event.receive.message.*;

public abstract class MessageReceivedEventHandler extends EventHandler {
    @Override
    public final String getName() {
        return "message-received";
    }

    public abstract void onMessageReceived(MessageReceivedEvent event);
}
