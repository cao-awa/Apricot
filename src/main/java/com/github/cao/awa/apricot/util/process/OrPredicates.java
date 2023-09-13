package com.github.cao.awa.apricot.util.process;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;

import java.util.List;
import java.util.function.Predicate;

public class OrPredicates<T> {
    private final List<Predicate<T>> predicates = ApricotCollectionFactor.arrayList();
    private final T target;

    public OrPredicates(T target) {
        this.target = target;
    }

    public OrPredicates<T> and(Predicate<T> predicate) {
        this.predicates.add(predicate);
        return this;
    }

    public static <X> OrPredicates<X> target(X target) {
        return new OrPredicates<>(target);
    }

    public boolean execute() {
        for (Predicate<T> predicate : this.predicates) {
            if (predicate.test(this.target)) {
                return true;
            }
        }
        return false;
    }
}
