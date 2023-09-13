package com.github.cao.awa.apricot.util.process;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class AndPredicates<T> {
    private final List<Predicate<T>> predicates = ApricotCollectionFactor.arrayList();
    private final T target;

    public AndPredicates(T target) {
        this.target = target;
    }

    public AndPredicates<T> and(Predicate<T> predicate) {
        this.predicates.add(predicate);
        return this;
    }

    public static <X> AndPredicates<X> target(X target) {
        return new AndPredicates<>(target);
    }

    public boolean execute() {
        for (Predicate<T> predicate : this.predicates) {
            if (!predicate.test(this.target)) {
                return false;
            }
        }
        return true;
    }
}
