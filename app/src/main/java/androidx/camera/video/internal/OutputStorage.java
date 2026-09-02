package androidx.camera.video.internal;

public interface OutputStorage {

    public interface Factory {
    }

    long getAvailableBytes();
}
