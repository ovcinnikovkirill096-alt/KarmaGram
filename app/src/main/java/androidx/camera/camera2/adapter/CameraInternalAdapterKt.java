package androidx.camera.camera2.adapter;

import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicInt;

public abstract class CameraInternalAdapterKt {
    private static final AtomicInt cameraAdapterIds = AtomicFU.atomic(0);

    public static final AtomicInt getCameraAdapterIds() {
        return cameraAdapterIds;
    }
}
