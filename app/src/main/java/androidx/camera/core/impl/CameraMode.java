package androidx.camera.core.impl;

public abstract class CameraMode {
    public static String toLabelString(int i) {
        if (i == 1) {
            return "CONCURRENT_CAMERA";
        }
        if (i == 2) {
            return "ULTRA_HIGH_RESOLUTION_CAMERA";
        }
        return "DEFAULT";
    }
}
