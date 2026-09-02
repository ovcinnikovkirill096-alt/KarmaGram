package androidx.camera.core;

public class CameraUnavailableException extends Exception {
    private final int mReason;

    public CameraUnavailableException(int i, String str) {
        super(str);
        this.mReason = i;
    }
}
