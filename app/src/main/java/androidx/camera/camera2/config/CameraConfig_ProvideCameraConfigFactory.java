package androidx.camera.camera2.config;

import dagger.internal.Preconditions;
import dagger.internal.Provider;

public abstract class CameraConfig_ProvideCameraConfigFactory implements Provider {
    public static CameraConfig provideCameraConfig(CameraConfig cameraConfig) {
        return (CameraConfig) Preconditions.checkNotNullFromProvides(cameraConfig.provideCameraConfig());
    }
}
