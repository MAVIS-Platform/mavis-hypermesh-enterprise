package com.hypermesh.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class RestAnnotations {
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RestController {}

    @Retention(RetentionPolicy.RUNTIME)
    public @interface GetMapping {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface PostMapping {
        String value();
    }
}
