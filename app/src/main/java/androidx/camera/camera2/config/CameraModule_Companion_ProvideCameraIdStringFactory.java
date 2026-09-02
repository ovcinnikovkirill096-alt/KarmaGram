package androidx.camera.camera2.config;

import dagger.internal.Preconditions;
import dagger.internal.Provider;

public abstract class CameraModule_Companion_ProvideCameraIdStringFactory implements Provider {
    public static String provideCameraIdString(CameraConfig cameraConfig) {
        return (String) Preconditions.checkNotNullFromProvides(CameraModule.Companion.provideCameraIdString(cameraConfig));
    }
}
