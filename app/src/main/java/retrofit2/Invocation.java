package retrofit2;

import j$.util.DesugarCollections;
import java.lang.reflect.Method;
import java.util.List;

public final class Invocation {
    private final List arguments;
    private final Object instance;
    private final Method method;
    private final Class service;

    Invocation(Class cls, Object obj, Method method, List list) {
        this.service = cls;
        this.instance = obj;
        this.method = method;
        this.arguments = DesugarCollections.unmodifiableList(list);
    }

    public Class service() {
        return this.service;
    }

    public Method method() {
        return this.method;
    }

    public String toString() {
        return String.format("%s.%s() %s", this.service.getName(), this.method.getName(), this.arguments);
    }
}
