package androidx.camera.camera2.pipe.config;

import androidx.camera.camera2.pipe.CameraBackend;
import androidx.camera.camera2.pipe.CameraGraph;
import androidx.camera.camera2.pipe.CameraMetadata;
import dagger.internal.Preconditions;
import dagger.internal.Provider;

public abstract class InternalCameraGraphModules_Companion_ProvideCameraMetadataFactory implements Provider {
    public static CameraMetadata provideCameraMetadata(CameraGraph.Config config, CameraBackend cameraBackend) {
        return (CameraMetadata) Preconditions.checkNotNullFromProvides(InternalCameraGraphModules.Companion.provideCameraMetadata(config, cameraBackend));
    }
}
