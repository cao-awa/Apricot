package com.github.cao.awa.apricot.message;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.utils.collection.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class AssembledMessage extends Message<MessageElement, AssembledMessage> {
    private static final MessageElement EMPTY_PLAINS_TEXT = new TextMessageElement(null);
    private final List<MessageElement> elements = ApricotCollectionFactor.newArrayList();
    private final List<AssembledMessage> incinerate = ApricotCollectionFactor.newArrayList();

    public AssembledMessage participate(MessageElement element) {
        incinerate(element);
        return this;
    }

    public AssembledMessage participateAll(List<MessageElement> elements) {
        elements.forEach(this::participate);
        return this;
    }

    public <T extends MessageElement, R> R handleMessage(Function<T, R> function, int index) {
        return function.apply((T) get(index));
    }

    @NotNull
    public MessageElement get(int index) {
        MessageElement element = this.elements.get(index);
        return element == null ? EMPTY_PLAINS_TEXT : element;
    }

    public <T extends MessageElement> T get(int index, Class<T> target) {
        MessageElement element = get(index);
        if (target.isInstance(element)) {
            return (T) element;
        }
        return null;
    }

    public String toPlainText() {
        StringBuilder builder = new StringBuilder();
        for (MessageElement element : this.elements) {
            builder.append(element.toPlainText());
        }
        return builder.toString();
    }

    public <T extends MessageElement> CarvedMessage<T> carver(Class<T> target) {
        CarvedMessage<T> carvedMessage = new CarvedMessage<>();
        this.elements.forEach(element -> {
            if (target.isInstance(element)) {
                carvedMessage.participate((T) element);
            }
        });
        return carvedMessage;
    }

    public void incinerateMessage(Consumer<AssembledMessage> action) {
        this.incinerate.forEach(action);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (MessageElement element : this.elements) {
            builder.append(element.getShortName());
        }
        return builder.toString();
    }

    public void incinerate(MessageElement element) {
        if (this.elements.size() > 0) {
            if (element.shouldIncinerate() || this.elements.get(this.elements.size() - 1).shouldIncinerate()) {
                this.incinerate.add(new AssembledMessage().participateAll(this.elements));
                this.elements.clear();
            }
        }
        this.elements.add(element);
    }
}
