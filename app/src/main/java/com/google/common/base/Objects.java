package com.google.common.base;

public abstract class Objects extends ExtraObjectsMethodsForWeb {
    public static boolean equal(Object obj, Object obj2) {
        return j$.util.Objects.equals(obj, obj2);
    }

    public static int hashCode(Object... objArr) {
        return j$.util.Objects.hash(objArr);
    }
}
