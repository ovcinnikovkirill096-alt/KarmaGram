package androidx.camera.core.impl;

public final class CameraUpdateException extends Exception {
    public CameraUpdateException(String str) {
        super(str);
    }

    public CameraUpdateException(String str, Throwable th) {
        super(str, th);
    }
}
