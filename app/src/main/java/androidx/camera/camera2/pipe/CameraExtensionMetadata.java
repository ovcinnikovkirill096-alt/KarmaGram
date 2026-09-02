package androidx.camera.camera2.pipe;

public interface CameraExtensionMetadata extends Metadata, UnsafeWrapper {
    boolean isPostviewSupported();
}
