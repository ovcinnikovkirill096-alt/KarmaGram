package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Descriptor {
    int objectTypeIndication() default -1;

    int[] tags();
}
