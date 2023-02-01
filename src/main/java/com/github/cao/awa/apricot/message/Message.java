package com.github.cao.awa.apricot.message;

public abstract class Message<T, M> {
    public abstract M participate(T element);

    public abstract String toPlainText();

    public abstract T get(int index);

    public abstract int size();
}
