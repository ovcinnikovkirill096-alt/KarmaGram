package androidx.camera.core.impl.utils.futures;

import com.google.common.util.concurrent.ListenableFuture;

public interface AsyncFunction {
    ListenableFuture apply(Object obj);
}
