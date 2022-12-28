package com.github.cao.awa.apricot.message;

import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class Message {
    private static final MessageElement EMPTY_PLAINS_TEXT = new TextMessageElement(null);
    private final List<MessageElement> elements = new ObjectArrayList<>();

    public Message participate(MessageElement element) {
        this.elements.add(element);
        return this;
    }

    public Message participateAll(List<MessageElement> elements) {
        this.elements.addAll(elements);
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

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (MessageElement element : this.elements) {
            builder.append(element.getShortName());
        }
        return builder.toString();
    }
}
