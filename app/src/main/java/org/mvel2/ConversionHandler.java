package org.mvel2;

public interface ConversionHandler {
    boolean canConvertFrom(Class cls);

    Object convertFrom(Object obj);
}
