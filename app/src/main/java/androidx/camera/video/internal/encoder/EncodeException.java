package androidx.camera.video.internal.encoder;

public class EncodeException extends Exception {
    private final int mErrorType;

    public EncodeException(int i, String str, Throwable th) {
        super(str, th);
        this.mErrorType = i;
    }
}
