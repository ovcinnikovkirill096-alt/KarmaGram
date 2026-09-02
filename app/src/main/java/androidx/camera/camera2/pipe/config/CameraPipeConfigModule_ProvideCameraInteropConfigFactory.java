package androidx.camera.camera2.pipe.config;

import androidx.camera.camera2.pipe.CameraPipe;
import dagger.internal.Preconditions;
import dagger.internal.Provider;

public abstract class CameraPipeConfigModule_ProvideCameraInteropConfigFactory implements Provider {
    public static CameraPipe.CameraInteropConfig provideCameraInteropConfig(CameraPipeConfigModule cameraPipeConfigModule, CameraPipe.Config config) {
        return (CameraPipe.CameraInteropConfig) Preconditions.checkNotNullFromProvides(cameraPipeConfigModule.provideCameraInteropConfig(config));
    }
}
