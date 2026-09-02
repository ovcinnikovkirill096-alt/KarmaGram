package kotlin.reflect;

import kotlin.jvm.functions.Function1;

public interface KProperty1 extends KProperty, Function1 {
    Object get(Object obj);
}
