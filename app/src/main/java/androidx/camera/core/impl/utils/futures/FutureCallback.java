package androidx.camera.core.impl.utils.futures;

public interface FutureCallback {
    void onFailure(Throwable th);

    void onSuccess(Object obj);
}
