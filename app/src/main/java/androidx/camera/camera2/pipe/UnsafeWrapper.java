package androidx.camera.camera2.pipe;

import kotlin.reflect.KClass;

public interface UnsafeWrapper {
    Object unwrapAs(KClass kClass);
}
