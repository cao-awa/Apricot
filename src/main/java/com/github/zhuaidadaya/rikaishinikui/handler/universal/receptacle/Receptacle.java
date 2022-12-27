package com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle;

public final class Receptacle<T> {
    private T target;

    public Receptacle(T target) {
        this.target = target;
    }

    public static <X> Receptacle<X> of(X target) {
        return new Receptacle<>(target);
    }

    public static <X> Receptacle<X> of() {
        return of(null);
    }

    public T get() {
        return this.target;
    }

    public Receptacle<T> set(T target) {
        this.target = target;
        return this;
    }
}
