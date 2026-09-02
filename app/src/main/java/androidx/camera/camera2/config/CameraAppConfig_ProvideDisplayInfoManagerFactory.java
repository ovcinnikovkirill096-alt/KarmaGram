package androidx.camera.camera2.config;

import android.content.Context;
import androidx.camera.camera2.impl.DisplayInfoManager;
import dagger.internal.Preconditions;
import dagger.internal.Provider;

public abstract class CameraAppConfig_ProvideDisplayInfoManagerFactory implements Provider {
    public static DisplayInfoManager provideDisplayInfoManager(CameraAppConfig cameraAppConfig, Context context) {
        return (DisplayInfoManager) Preconditions.checkNotNullFromProvides(cameraAppConfig.provideDisplayInfoManager(context));
    }
}
