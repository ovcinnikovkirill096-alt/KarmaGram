package androidx.camera.camera2.config;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class CameraConfig {
    private final String cameraId;

    public /* synthetic */ CameraConfig(String str, DefaultConstructorMarker defaultConstructorMarker) {
        this(str);
    }

    public final CameraConfig provideCameraConfig() {
        return this;
    }

    private CameraConfig(String cameraId) {
        Intrinsics.checkNotNullParameter(cameraId, "cameraId");
        this.cameraId = cameraId;
    }

    /* JADX INFO: renamed from: getCameraId-Dz_R5H8, reason: not valid java name */
    public final String m48getCameraIdDz_R5H8() {
        return this.cameraId;
    }
}
