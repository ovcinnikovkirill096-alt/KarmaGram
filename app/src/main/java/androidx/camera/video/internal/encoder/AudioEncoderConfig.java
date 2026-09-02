package androidx.camera.video.internal.encoder;

public abstract class AudioEncoderConfig implements EncoderConfig {
    public abstract int getCaptureSampleRate();

    public abstract int getEncodeSampleRate();
}
