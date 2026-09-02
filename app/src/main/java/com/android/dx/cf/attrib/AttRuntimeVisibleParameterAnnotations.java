package com.android.dx.cf.attrib;

import com.android.dx.rop.annotation.AnnotationsList;

public final class AttRuntimeVisibleParameterAnnotations extends BaseParameterAnnotations {
    public static final String ATTRIBUTE_NAME = "RuntimeVisibleParameterAnnotations";

    public AttRuntimeVisibleParameterAnnotations(AnnotationsList annotationsList, int i) {
        super(ATTRIBUTE_NAME, annotationsList, i);
    }
}
