package com.github.cao.awa.apricot.message.assemble;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.message.*;
import com.github.cao.awa.apricot.message.carve.*;
import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.message.element.plain.text.*;
import com.github.cao.awa.apricot.util.collection.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

@Stable
public class AssembledMessage extends Message<MessageElement, AssembledMessage> {
    private static final MessageElement EMPTY_PLAINS_TEXT = new TextMessageElement("");
    private final List<MessageElement> elements;
    private final List<AssembledMessage> incinerate;

    public AssembledMessage(List<MessageElement> elements, List<AssembledMessage> incinerate) {
        this.elements = elements;
        this.incinerate = incinerate;
    }

    public AssembledMessage(List<MessageElement> elements) {
        this.elements = elements;
        this.incinerate = ApricotCollectionFactor.newArrayList();
    }

    public AssembledMessage() {
        this.elements = ApricotCollectionFactor.newArrayList();
        this.incinerate = ApricotCollectionFactor.newArrayList();
    }

    public AssembledMessage participate(MessageElement element) {
        incinerate(element);
        return this;
    }

    public String toPlainText() {
        StringBuilder builder = new StringBuilder();
        for (MessageElement element : this.elements) {
            builder.append(element.toPlainText());
        }
        for (AssembledMessage message : this.incinerate) {
            builder.append(message.toPlainText());
        }
        return builder.toString();
    }

    @NotNull
    public MessageElement get(int index) {
        MessageElement element = this.elements.get(index);
        return element == null ? EMPTY_PLAINS_TEXT : element;
    }

    @Override
    public int size() {
        int size = 0;
        size += this.elements.size();
        for (AssembledMessage message : this.incinerate) {
            size += message.size();
        }
        return size;
    }

    public AssembledMessage participateAll(List<MessageElement> elements) {
        elements.forEach(this::participate);
        return this;
    }

    public <T extends MessageElement, R> R handleMessage(Function<T, R> function, int index) {
        return function.apply((T) get(index));
    }

    public <T extends MessageElement> T get(int index, Class<T> target) {
        MessageElement element = get(index);
        if (target.isInstance(element)) {
            return (T) element;
        }
        return null;
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

    public <T extends MessageElement> AssembledMessage skip(Class<T> target) {
        for (int i = 0; i < this.elements.size(); i++) {
            if (target.isInstance(this.elements.get(i))) {
                this.elements.remove(i);
            } else {
                break;
            }
        }
        return this;
    }

    public <T extends MessageElement> AssembledMessage skipTo(Class<T> target) {
        AssembledMessage message = new AssembledMessage(
                this.elements,
                this.incinerate
        );
        message.skip(target);
        return message;
    }

    public <T extends MessageElement> AssembledMessage clear(Class<T> target) {
        this.elements.removeIf(target::isInstance);
        return this;
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
            if (element.shouldIncinerate() || this.elements.get(this.elements.size() - 1)
                                                           .shouldIncinerate()) {
                this.incinerate.add(new AssembledMessage().participateAll(this.elements));
                this.elements.clear();
            }
        }
        this.elements.add(element);
    }

    public void forEach(Consumer<MessageElement> consumer) {
        this.incinerate.forEach(message -> message.forEach(consumer));

        this.elements.forEach(consumer);
    }
}
