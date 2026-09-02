package org.mvel2;

public class ConversionException extends RuntimeException {
    public ConversionException(String str) {
        super(str);
    }

    public ConversionException(String str, Throwable th) {
        super(str, th);
    }
}
