package com.diting.error;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorDef {
    int httpStatusCode();

    String message() default "";

    String code();

    String field() default "";

    String reason() default "";
}
