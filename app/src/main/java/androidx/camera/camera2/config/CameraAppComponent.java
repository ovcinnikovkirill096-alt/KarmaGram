package androidx.camera.camera2.config;

import androidx.camera.camera2.pipe.CameraDevices;
import androidx.camera.camera2.pipe.CameraPipe;

public interface CameraAppComponent {

    public interface Builder {
        CameraAppComponent build();

        Builder config(CameraAppConfig cameraAppConfig);
    }

    CameraComponent.Builder cameraBuilder();

    CameraDevices getCameraDevices();

    CameraPipe getCameraPipe();
}
