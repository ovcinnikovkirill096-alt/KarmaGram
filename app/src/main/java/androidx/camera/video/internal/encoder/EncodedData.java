package androidx.camera.video.internal.encoder;

public interface EncodedData extends AutoCloseable {
    @Override // java.lang.AutoCloseable
    void close();

    long getPresentationTimeUs();

    boolean isKeyFrame();

    long size();
}
