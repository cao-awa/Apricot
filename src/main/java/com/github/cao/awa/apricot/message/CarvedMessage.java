package com.github.cao.awa.apricot.message;

import com.github.cao.awa.apricot.message.element.*;
import com.github.cao.awa.apricot.utils.collection.*;

import java.util.*;

public class CarvedMessage<T extends MessageElement> extends Message<T, CarvedMessage<T>> {
    private final List<T> elements = ApricotCollectionFactor.newArrayList();

    public T get(int index) {
        return this.elements.get(index);
    }

    @Override
    public CarvedMessage<T> participate(T element) {
        this.elements.add(element);
        return this;
    }

    public String toPlainText() {
        StringBuilder builder = new StringBuilder();
        for (T element : this.elements) {
            builder.append(element.toPlainText());
        }
        return builder.toString();
    }
}
