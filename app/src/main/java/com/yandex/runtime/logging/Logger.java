package com.yandex.runtime.logging;

public class Logger {
    public static native void debug(String str);

    public static native void error(String str);

    public static native void info(String str);

    public static native void warn(String str);
}
