package com.github.cao.awa.apricot.plugin.internal.simple;

import com.github.cao.awa.apricot.event.receive.message.group.received.*;
import com.github.cao.awa.apricot.event.receive.message.group.sent.*;
import com.github.cao.awa.apricot.event.receive.message.personal.received.*;
import com.github.cao.awa.apricot.event.receive.message.personal.sent.*;
import com.github.cao.awa.apricot.event.receive.message.recall.*;
import com.github.cao.awa.apricot.event.receive.message.recall.group.*;
import com.github.cao.awa.apricot.event.receive.message.recall.personal.*;
import com.github.cao.awa.apricot.event.receive.poke.*;
import com.github.cao.awa.apricot.plugin.*;
import com.github.cao.awa.apricot.plugin.internal.simple.handlers.message.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class LawnBus extends Plugin {
    private final MessageBus message = new MessageBus();
    private final PokeBus poke = new PokeBus();
    private final RecallBus recall = new RecallBus();

    @Override
    public @NotNull String getName() {
        return "生草机巴士";
    }

    @Override
    public void onInitialize() {
        registerHandlers(
                this.message,
                this.recall,
                this.poke
        );
    }

    public void privateFriendMsgRe(Consumer<PrivateFriendMessageReceivedEvent> action) {
        this.message.register(action);
    }

    public void privateMsgRe(Consumer<PrivateMessageReceivedEvent<?>> action) {
        this.message.register(action);
    }

    public void groupAnonymousMsgRe(Consumer<GroupAnonymousMessageReceivedEvent> action) {
        this.message.register(action);
    }

    public void groupNormalMsgRe(Consumer<GroupNormalMessageReceivedEvent> action) {
        this.message.register(action);
    }

    public void groupMsgRe(Consumer<GroupMessageReceivedEvent<?>> action) {
        this.message.register(action);
    }

    public void privateFriendMsgSe(Consumer<PrivateFriendMessageSentEvent> action) {
        this.message.register(action);
    }

    public void privateMsgSe(Consumer<PrivateMessageSentEvent<?>> action) {
        this.message.register(action);
    }

    public void groupAnonymousMsgSe(Consumer<GroupAnonymousMessageSentEvent> action) {
        this.message.register(action);
    }

    public void groupNormalMsgSe(Consumer<GroupNormalMessageSentEvent> action) {
        this.message.register(action);
    }

    public void groupMsgSe(Consumer<GroupMessageSentEvent<?>> action) {
        this.message.register(action);
    }

    public void privatePoke(Consumer<PrivatePokeReceivedEvent> action) {
        this.poke.register(action);
    }

    public void groupPoke(Consumer<GroupPokeReceivedEvent> action) {
        this.poke.register(action);
    }

    public void poke(Consumer<PokeReceivedEvent<?>> action) {
        this.poke.register(action);
    }

    public void groupMsgRecall(Consumer<GroupMessageRecallEvent> action) {
        this.recall.register(action);
    }

    public void privateMsgRecall(Consumer<PrivateMessageRecallEvent> action) {
        this.recall.register(action);
    }

    public void msgRecall(Consumer<MessageRecallEvent<?>> action) {
        this.recall.register(action);
    }

    @Override
    public String version() {
        return "0.0.1";
    }
}