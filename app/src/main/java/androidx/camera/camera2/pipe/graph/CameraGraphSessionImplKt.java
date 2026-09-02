package androidx.camera.camera2.pipe.graph;

import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicInt;

public abstract class CameraGraphSessionImplKt {
    private static final AtomicInt cameraGraphSessionIds = AtomicFU.atomic(0);

    public static final AtomicInt getCameraGraphSessionIds() {
        return cameraGraphSessionIds;
    }
}
