package com.github.cao.awa.apricot.message.forward.dummy.node;

import com.github.cao.awa.apricot.message.forward.dummy.DummyForwardMessage;

import java.util.function.Consumer;

public class DummyForwardNode {
    private final Object obj;

    public DummyForwardNode(Object obj) {
        this.obj = obj;
    }

    public void ifMessage(Consumer<DummyForwardMessage> consumer) {
        if (this.obj instanceof DummyForwardMessage message) {
            consumer.accept(message);
        }
    }

    public void ifTree(Consumer<DummyForwardTree> consumer) {
        if (this.obj instanceof DummyForwardTree message) {
            consumer.accept(message);
        }
    }

    public void forEach(Consumer<DummyForwardMessage> consumer) {
        ifTree(tree -> tree.forEach(node -> node.forEach(consumer)));
        ifMessage(consumer);
    }

    public void forEach(Consumer<DummyForwardTree> inner, Consumer<DummyForwardMessage> consumer) {
        ifTree(tree -> {
            inner.accept(tree);
            tree.forEach(node -> node.forEach(consumer));
        });
        ifMessage(consumer);
    }
}
