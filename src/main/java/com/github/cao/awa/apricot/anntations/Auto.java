package com.github.cao.awa.apricot.anntations;

import java.lang.annotation.*;

@Auto
@Stable
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Auto {
}
