package androidx.camera.camera2.pipe.config;

import android.content.Context;
import androidx.camera.camera2.pipe.compat.DevicePolicyManagerWrapper;
import dagger.internal.Preconditions;
import dagger.internal.Provider;

public abstract class CameraPipeModule_Companion_ProvideDevicePolicyManagerWrapperFactory implements Provider {
    public static DevicePolicyManagerWrapper provideDevicePolicyManagerWrapper(Context context) {
        return (DevicePolicyManagerWrapper) Preconditions.checkNotNullFromProvides(CameraPipeModule.Companion.provideDevicePolicyManagerWrapper(context));
    }
}
