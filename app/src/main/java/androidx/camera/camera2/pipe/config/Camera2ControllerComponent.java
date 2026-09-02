package androidx.camera.camera2.pipe.config;

import androidx.camera.camera2.pipe.CameraController;

public interface Camera2ControllerComponent {

    public interface Builder {
        Camera2ControllerComponent build();

        Builder camera2ControllerConfig(Camera2ControllerConfig camera2ControllerConfig);
    }

    CameraController cameraController();
}
