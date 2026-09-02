package androidx.camera.camera2.pipe.core;

import kotlinx.coroutines.sync.Mutex;
import kotlinx.coroutines.sync.MutexKt;

public final class CoroutineMutex {
    private final Mutex mutex = MutexKt.Mutex$default(false, 1, null);

    public final Mutex getMutex$camera_camera2_pipe() {
        return this.mutex;
    }
}
