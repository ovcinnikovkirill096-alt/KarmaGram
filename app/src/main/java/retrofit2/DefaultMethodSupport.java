package retrofit2;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

abstract class DefaultMethodSupport {
    private static Constructor lookupConstructor;

    static Object invoke(Method method, Class cls, Object obj, Object[] objArr) throws NoSuchMethodException {
        Constructor declaredConstructor = lookupConstructor;
        if (declaredConstructor == null) {
            declaredConstructor = DefaultMethodSupport$$ExternalSyntheticApiModelOutline0.m().getDeclaredConstructor(Class.class, Integer.TYPE);
            declaredConstructor.setAccessible(true);
            lookupConstructor = declaredConstructor;
        }
        return DefaultMethodSupport$$ExternalSyntheticApiModelOutline1.m(declaredConstructor.newInstance(cls, -1)).unreflectSpecial(method, cls).bindTo(obj).invokeWithArguments(objArr);
    }
}
