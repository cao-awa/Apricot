package com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function;

@FunctionalInterface
public interface ExceptingFunction<T, R> {
    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t) throws Exception;
}

