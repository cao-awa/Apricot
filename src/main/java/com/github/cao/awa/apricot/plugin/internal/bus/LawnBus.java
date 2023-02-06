package com.github.cao.awa.apricot.plugin.internal.bus;

import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import com.github.cao.awa.apricot.event.receive.message.group.sent.*;
import com.github.cao.awa.apricot.event.receive.message.personal.received.*;
import com.github.cao.awa.apricot.event.receive.message.personal.sent.*;
import com.github.cao.awa.apricot.event.receive.message.recall.*;
import com.github.cao.awa.apricot.event.receive.message.recall.group.*;
import com.github.cao.awa.apricot.event.receive.message.recall.personal.*;
import com.github.cao.awa.apricot.event.receive.poke.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.plugin.internal.bus.handlers.message.*;
import com.github.cao.awa.apricot.plugin.name.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class LawnBus extends Plugin {
    private static final MessageBus message = new MessageBus();
    private static final PokeBus poke = new PokeBus();
    private static final RecallBus recall = new RecallBus();

    @Override
    public @NotNull PluginName name() {
        return PluginName.of(
                "Lawn_bus",
                "生草机巴士"
        );
    }

    @Override
    public void onInitialize() {
        registerHandlers(
                message,
                recall,
                poke
        );
    }

    @Override
    public String version() {
        return "1.0.1";
    }

    public void privateFriendMsgRe(Consumer<PrivateFriendMessageReceivedEvent> action) {
        message.register(action);
    }

    public void privateMsgRe(Consumer<PrivateMessageReceivedEvent<?>> action) {
        message.register(action);
    }

    public void groupAnonymousMsgRe(Consumer<GroupAnonymousMessageReceivedEvent> action) {
        message.register(action);
    }

    public void groupNormalMsgRe(Consumer<GroupNormalMessageReceivedEvent> action) {
        message.register(action);
    }

    public void groupMsgRe(Consumer<GroupMessageReceivedEvent<?>> action) {
        message.register(action);
    }

    public void privateFriendMsgSe(Consumer<PrivateFriendMessageSentEvent> action) {
        message.register(action);
    }

    public void privateMsgSe(Consumer<PrivateMessageSentEvent<?>> action) {
        message.register(action);
    }

    public void groupAnonymousMsgSe(Consumer<GroupAnonymousMessageSentEvent> action) {
        message.register(action);
    }

    public void groupNormalMsgSe(Consumer<GroupNormalMessageSentEvent> action) {
        message.register(action);
    }

    public void groupMsgSe(Consumer<GroupMessageSentEvent<?>> action) {
        message.register(action);
    }

    public void privatePoke(Consumer<PrivatePokeReceivedEvent> action) {
        poke.register(action);
    }

    public void groupPoke(Consumer<GroupPokeReceivedEvent> action) {
        poke.register(action);
    }

    public void poke(Consumer<PokeReceivedEvent<?>> action) {
        poke.register(action);
    }

    public void groupMsgRecall(Consumer<GroupMessageRecallEvent> action) {
        recall.register(action);
    }

    public void privateMsgRecall(Consumer<PrivateMessageRecallEvent> action) {
        recall.register(action);
    }

    public void msgRecall(Consumer<MessageRecallEvent<?>> action) {
        recall.register(action);
    }
}
