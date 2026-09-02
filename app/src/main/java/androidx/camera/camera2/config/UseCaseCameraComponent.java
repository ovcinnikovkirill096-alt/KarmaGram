package androidx.camera.camera2.config;

import androidx.camera.camera2.impl.UseCaseCamera;

public interface UseCaseCameraComponent {

    public interface Builder {
        UseCaseCameraComponent build();

        Builder config(UseCaseCameraConfig useCaseCameraConfig);
    }

    UseCaseCamera getUseCaseCamera();
}
