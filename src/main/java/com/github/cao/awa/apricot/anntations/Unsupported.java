package com.github.cao.awa.apricot.anntations;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Stable
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, MODULE, PARAMETER, TYPE})
public @interface Unsupported {
}
