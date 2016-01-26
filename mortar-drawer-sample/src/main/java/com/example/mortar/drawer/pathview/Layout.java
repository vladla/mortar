package com.example.mortar.drawer.pathview;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Lubenets Vladyslav on 1/20/16.
 */
@Retention(RUNTIME) @Target(TYPE)
public @interface Layout {
    int value();
}
