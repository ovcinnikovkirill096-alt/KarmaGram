package com.sun.jna;

import j$.util.DesugarCollections;
import java.util.Arrays;
import java.util.List;

public interface Callback {
    public static final List<String> FORBIDDEN_NAMES = DesugarCollections.unmodifiableList(Arrays.asList("hashCode", "equals", "toString"));
    public static final String METHOD_NAME = "callback";

    public interface UncaughtExceptionHandler {
        void uncaughtException(Callback callback, Throwable th);
    }
}
