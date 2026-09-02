package androidx.camera.camera2.pipe.config;

import androidx.camera.camera2.pipe.CameraPipe;
import dagger.internal.Preconditions;
import dagger.internal.Provider;

public abstract class CameraPipeConfigModule_ProvideCameraPipeFlagsFactory implements Provider {
    public static CameraPipe.Flags provideCameraPipeFlags(CameraPipeConfigModule cameraPipeConfigModule) {
        return (CameraPipe.Flags) Preconditions.checkNotNullFromProvides(cameraPipeConfigModule.provideCameraPipeFlags());
    }
}
