package androidx.camera.camera2.pipe.compat;

import kotlin.coroutines.Continuation;

public interface CameraAvailabilityMonitor {

    public interface Session extends AutoCloseable {
        Object awaitAvailableCamera(long j, Continuation continuation);
    }

    /* JADX INFO: renamed from: startMonitoring-0r8Bogc */
    Object mo443startMonitoring0r8Bogc(String str, Continuation continuation);
}
