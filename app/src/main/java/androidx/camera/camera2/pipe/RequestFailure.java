package androidx.camera.camera2.pipe;

public interface RequestFailure extends UnsafeWrapper {
    int getReason();

    boolean getWasImageCaptured();
}
