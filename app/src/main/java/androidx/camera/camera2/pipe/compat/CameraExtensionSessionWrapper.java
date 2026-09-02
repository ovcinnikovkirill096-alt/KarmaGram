package androidx.camera.camera2.pipe.compat;

import androidx.camera.camera2.pipe.UnsafeWrapper;

public interface CameraExtensionSessionWrapper extends CameraCaptureSessionWrapper, UnsafeWrapper, AutoCloseable {

    public interface StateCallback extends SessionStateCallback {
        void onClosed(CameraExtensionSessionWrapper cameraExtensionSessionWrapper);

        void onConfigureFailed(CameraExtensionSessionWrapper cameraExtensionSessionWrapper);

        void onConfigured(CameraExtensionSessionWrapper cameraExtensionSessionWrapper);
    }
}
